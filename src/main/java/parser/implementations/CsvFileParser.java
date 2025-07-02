package parser.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.fs.Path;

import common.constants.SupportingExtensionContants;
import common.exceptions.YadConverterException;
import common.models.AvroDataCarrier;
import common.utils.StringHelper;
import converter.constants.FileConstants;
import parser.abstractions.IFileParser;

public class CsvFileParser implements IFileParser {

    public String getSupportedExtension() {
        return SupportingExtensionContants.CSV;
    }

    public AvroDataCarrier parseToAvro(String inputFilePath) throws YadConverterException {
        Path csvPath = new Path(inputFilePath);
        CSVFormat csvFormat = CSVFormat.DEFAULT
            .builder()
            .setDelimiter(FileConstants.CSV_DELIMITER)
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();

        List<String> headers;
        List<CSVRecord> records;

        try (
            Reader inputStream = new InputStreamReader(new FileInputStream(csvPath.toString()), StandardCharsets.UTF_8);
            CSVParser parser = csvFormat.parse(inputStream);
        ) {
            headers = getCsvHeaders(parser);

            // Read all records into memory to infer schema.
            // For very large files, this would need to be optimized for streaming.
            records = parser.getRecords();

        } catch (FileNotFoundException ex) {
            throw new YadConverterException("Input file not found.");
        } catch (IOException ex) {
            throw new YadConverterException("Cannot parse input file.");
        }

        Schema avroSchema = inferAvroSchemaFromCsv(headers, records);
        List<GenericRecord> avroRecords = retriveAvroRecords(avroSchema, headers, records);

        return new AvroDataCarrier(avroRecords);
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

    private List<GenericRecord> retriveAvroRecords(Schema avroSchema, List<String> headers, List<CSVRecord> records) throws YadConverterException {
        List<GenericRecord> data = new ArrayList<>();

        for (CSVRecord csvRecord : records) {
            GenericRecord avroRecord = new GenericData.Record(avroSchema);

            for (String header : headers) {
                Object value = csvRecord.get(header);
                Schema.Field field = avroSchema.getField(header);

                if (field == null) {
                    // Fallback for inappropriate schema
                    avroRecord.put(header, value);
                    continue;
                }

                // Attempt to convert value to the inferred type
                avroRecord.put(header, convertValueToAvroField(value, field.schema()));
            }

            data.add(avroRecord);
        }

        return data;
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
                return StringHelper.convertToLong(stringValue);
            case DOUBLE:
                return StringHelper.convertToDouble(stringValue);
            case BOOLEAN:
                return StringHelper.convertToBoolean(stringValue);
            default:
                throw new YadConverterException("Unsupported Avro type for conversion: ", avroSchema.getType());
        }
    }

}
