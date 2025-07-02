package converter.steps;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;

import common.constants.SupportingExtensionContants;
import common.exceptions.YadConverterException;
import converter.abstractions.ConverterPipelineStepBase;
import converter.constants.FileConstants;
import converter.models.ConverterPipelineData;

public class ParquetToCsvConversionStep extends ConverterPipelineStepBase {
    public void run(ConverterPipelineData data) throws YadConverterException {
        boolean isParquetToCsvConversion =
            data.getInputFileExtension().equalsIgnoreCase(SupportingExtensionContants.PARQUET)
            && data.getOutputFileExtension().equalsIgnoreCase(SupportingExtensionContants.CSV);

        if (!isParquetToCsvConversion) {
            nextStep.run(data);
            return;
        }

        convertToCsv(data.getInputFilePath(), data.getOutputFilePath());
    }

    /**
     * Converts a Parquet file to a CSV file. Reads Parquet records and writes them
     * to a CSV file.
     *
     * @param parquetFilePath The path to the input Parquet file.
     * @param csvFilePath     The path to the output CSV file.
     * @throws IOException If an I/O error occurs during file reading or writing.
     */
    private static void convertToCsv(String parquetFilePath, String csvFilePath) throws YadConverterException {
        Path csvPath = new Path(csvFilePath);
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setDelimiter(FileConstants.CSV_DELIMITER)
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();

        try {
            InputFile parquetFile = HadoopInputFile.fromPath(new Path(parquetFilePath), new Configuration());

            try (
                ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(parquetFile).build();
                BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(csvPath.toString()), StandardCharsets.UTF_8));
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
            ) {

                GenericRecord record;
                List<String> headers = null;

                // Read records one by one
                while ((record = reader.read()) != null) {
                    if (headers == null) {
                        headers = getHeaderFromParquetField(record.getSchema().getFields());
                        printCsvRow(headers, csvPrinter);
                    }

                    List<String> csvRow = getCsvRowFromParquetRecord(record, headers);
                    printCsvRow(csvRow, csvPrinter);
                }
            }
        } catch (IOException ex) {
            throw new YadConverterException("Input file not found.");
        }
    }

    private static List<String> getCsvRowFromParquetRecord(GenericRecord record, List<String> headers) {
        return headers.stream()
            .map(h -> record.get(h))
            .map(v -> v != null ? v.toString() : "")
            .collect(Collectors.toList());
    }

    private static List<String> getHeaderFromParquetField(List<Schema.Field> parquetFields) {
        return parquetFields.stream()
            .map(Schema.Field::name)
            .collect(Collectors.toList());
    }

    private static void printCsvRow(Iterable<?> csvRow, CSVPrinter csvPrinter) throws YadConverterException {
        try {
            csvPrinter.printRecord(csvRow);
        } catch (IOException ex) {
            throw new YadConverterException("Error while trying to print CSV.");
        }
    }
}
