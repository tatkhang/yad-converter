package writer.implementations;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.apache.hadoop.fs.Path;

import common.constants.SupportingExtensionContants;
import common.exceptions.YadConverterException;
import common.models.AvroDataCarrier;
import converter.constants.FileConstants;
import writer.abstractions.IFileWriter;

public class CsvFileWriter implements IFileWriter {

    public String getSupportedExtension() {
        return SupportingExtensionContants.CSV;
    }

    public void writeFromAvro(String outputFilePath, AvroDataCarrier data) throws YadConverterException {
        Path csvPath = new Path(outputFilePath);
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setDelimiter(FileConstants.CSV_DELIMITER)
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();

        try (
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(csvPath.toString()), StandardCharsets.UTF_8));
            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
        ) {

                List<String> headers = getHeaderFromAvroSchema(data.getSchema());
                printCsvRow(headers, csvPrinter);

                for (GenericRecord avroRecord : data.getRecords()) {
                    List<String> csvRow = getCsvRowFromAvroRecord(avroRecord, headers);
                    printCsvRow(csvRow, csvPrinter);
                }

        } catch (IOException ex) {
            throw new YadConverterException("Input file not found.");
        }
    }

    private static List<String> getHeaderFromAvroSchema(Schema avroSchema) {
        return avroSchema.getFields().stream()
            .map(Schema.Field::name)
            .collect(Collectors.toList());
    }

    private static List<String> getCsvRowFromAvroRecord(GenericRecord record, List<String> headers) {
        return headers.stream()
            .map(h -> record.get(h))
            .map(v -> v != null ? v.toString() : "")
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
