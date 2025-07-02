package converter.steps;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import org.apache.commons.csv.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.apache.parquet.io.OutputFile;

import common.constants.SupportingExtensionContants;
import common.exceptions.YadConverterException;
import common.utils.StringHelper;
import converter.abstractions.ConverterPipelineStepBase;
import converter.constants.FileConstants;
import converter.models.ConverterPipelineData;

public class CsvToParquetConversionStep extends ConverterPipelineStepBase {
    public void run(ConverterPipelineData data) throws YadConverterException {
        boolean isCsvToParquetConversion =
            data.getInputFileExtension().equalsIgnoreCase(SupportingExtensionContants.CSV)
            && data.getOutputFileExtension().equalsIgnoreCase(SupportingExtensionContants.PARQUET);

        if (!isCsvToParquetConversion) {
            tryRunNextStep(data);
            return;
        }

        convertToParquet(data.getInputFilePath(), data.getOutputFilePath());
    }

    private void convertToParquet(String csvFilePath, String parquetFilePath) throws YadConverterException {
        Path csvPath = new Path(csvFilePath);
        CSVFormat csvFormat = CSVFormat.DEFAULT
            .builder()
            .setDelimiter(FileConstants.CSV_DELIMITER)
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();

        try (
            Reader inputStream = new InputStreamReader(new FileInputStream(csvPath.toString()), StandardCharsets.UTF_8);
            CSVParser parser = csvFormat.parse(inputStream);
        ) {
            List<String> headers = getCsvHeaders(parser);

            // Read all records into memory to infer schema and then write to Parquet.
            // For very large files, this would need to be optimized for streaming.
            List<CSVRecord> records = parser.getRecords();

            Schema avroSchema = inferAvroSchemaFromCsv(headers, records);

            writeOutputFile(parquetFilePath, headers, records, avroSchema);
        } catch (FileNotFoundException ex) {
            throw new YadConverterException("Input file not found.");
        } catch (IOException ex) {
            throw new YadConverterException("Cannot parse input file.");
        }
    }

    private void writeOutputFile(String parquetFilePath, List<String> headers, List<CSVRecord> records,
            Schema avroSchema) throws IOException, YadConverterException {

        OutputFile parquetFile = HadoopOutputFile.fromPath(new Path(parquetFilePath), new Configuration());

        try (ParquetWriter<GenericRecord> writer = AvroParquetWriter.<GenericRecord>builder(parquetFile)
            .withSchema(avroSchema)
            .withCompressionCodec(CompressionCodecName.SNAPPY) // Snappy for good balance of speed/compression
            .build()) {

            // Iterate through CSV records and write to Parquet
            for (CSVRecord csvRecord : records) {
                GenericRecord avroRecord = new GenericData.Record(avroSchema);

                for (String header : headers) {
                    Object value = csvRecord.get(header);
                    Schema.Field field = avroSchema.getField(header);

                    if (field != null) {
                        // Attempt to convert value to the inferred type
                        avroRecord.put(header, convertValueToAvroField(value, field.schema()));
                    } else {
                        // This should ideally not happen if schema inference is correct
                        avroRecord.put(header, value);
                    }
                }

                writer.write(avroRecord);
            }
        } catch (IOException ex) {
            throw new YadConverterException("Output file already exists.");
        }
    }

    private List<String> getCsvHeaders(CSVParser parser) throws YadConverterException {
        List<String> headers = parser.getHeaderNames();

        if (headers == null || headers.isEmpty()) {
            throw new YadConverterException("CSV file must have a header row for schema inference.");
        }

        return headers;
    }

    /**
     * Infers the Avro schema from CSV headers and a sample of records.
     * Attempts to determine the most appropriate data type for each column.
     *
     * @param headers List of column headers from the CSV.
     * @param records List of CSV records (used for type inference).
     * @return An Avro Schema representing the CSV data.
     * @implNote Support primitive data types only
     */
    private Schema inferAvroSchemaFromCsv(List<String> headers, List<CSVRecord> records) {
        SchemaBuilder.FieldAssembler<Schema> fieldAssembler = SchemaBuilder.record("CsvRecord").fields();

        for (String columnName : headers) {
            boolean isEntireColumnBoolean = true;
            boolean isEntireColumnLong = true;
            boolean isEntireColumnDouble = true;
            boolean isNullable = false;

            for (CSVRecord row : records) {
                String value = row.get(columnName);
                if (StringHelper.isNullOrWhiteSpace(value)) {
                    isNullable = true;
                    continue;
                }

                if (!StringHelper.isParseableAsBoolean(value)) {
                    isEntireColumnBoolean = false;
                }

                if (!StringHelper.isParseableAsLong(value)) {
                    isEntireColumnLong = false;
                }

                if (!StringHelper.isParseableAsDouble(value)) {
                    isEntireColumnDouble = false;
                }

                // If at any point all types are false, the column type is fallback to String, no need to check further
                if ( !isEntireColumnBoolean && !isEntireColumnLong && !isEntireColumnDouble) {
                    break;
                }
            }

            SchemaBuilder.BaseFieldTypeBuilder<Schema> fieldBuilder = isNullable
                ? fieldAssembler.name(columnName).type().nullable()
                : fieldAssembler.name(columnName).type();

            // Prioritize specific types: Boolean -> Long -> Double -> String
            if (isEntireColumnBoolean) {
                fieldAssembler = fieldBuilder.booleanType().noDefault();
            } else if (isEntireColumnLong) {
                fieldAssembler = fieldBuilder.longType().noDefault();
            } else if (isEntireColumnDouble) {
                fieldAssembler = fieldBuilder.doubleType().noDefault();
            } else {
                fieldAssembler = fieldBuilder.stringType().noDefault();
            }
        }

        return fieldAssembler.endRecord();
    }

    /**
     * Converts a value to the target Avro field type.
     * Handles null values for nullable types.
     *
     * @param value The object value from CSV.
     * @param avroSchema The Avro schema for the field.
     * @return The converted object or null if conversion is not possible and schema allows null.
     * @throws YadConverterException 
     * @implNote Support primitive data types only
     */
    private Object convertValueToAvroField(Object value, Schema avroSchema) throws YadConverterException {
        String stringValue = (value == null) ? "" : String.valueOf(value).trim();

        if (avroSchema.getType() == Schema.Type.UNION) {
            // Handle nullable types (union of null and actual type)
            for (Schema subSchema : avroSchema.getTypes()) {
                if (subSchema.getType() != Schema.Type.NULL) {
                    // Try to convert to the non-null type
                    return convertValueToAvroField(stringValue, subSchema);
                }

                if (stringValue.isEmpty()) {
                    return null;
                }
            }

            // Fallback for inappropriate schema
            return null;
        }

        if (stringValue.isEmpty()) {
            // If not a union and empty, return null
            return null;
        }

        switch (avroSchema.getType()) {
            case STRING:
                return stringValue;
            case LONG:
                return convertToLong(stringValue);
            case DOUBLE:
                return convertToDouble(stringValue);
            case BOOLEAN:
                return convertToBoolean(stringValue);
            default:
                throw new YadConverterException("Unsupported Avro type for conversion: ", avroSchema.getType());
        }
    }

    private static Object convertToLong(String stringValue) throws YadConverterException {
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException ex) {
            throw new YadConverterException("Could not convert %s to LONG.", stringValue);
        }
    }

    private static Object convertToDouble(String stringValue) throws YadConverterException {
        try {
            return Double.parseDouble(stringValue);
        } catch (NumberFormatException ex) {
            throw new YadConverterException("Could not convert %s to DOUBLE.", stringValue);
        }
    }

    private static Object convertToBoolean(String stringValue) throws YadConverterException {
        if ("true".equalsIgnoreCase(stringValue) || "1".equals(stringValue)) {
            return true;
        }

        if ("false".equalsIgnoreCase(stringValue) || "0".equals(stringValue)) {
            return false;
        }

        throw new YadConverterException("Could not convert %s to BOOLEAN.", stringValue);
    }

}
