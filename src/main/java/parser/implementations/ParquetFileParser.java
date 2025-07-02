package parser.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;

import common.constants.SupportingExtensionContants;
import common.exceptions.YadConverterException;
import common.models.AvroDataCarrier;
import parser.abstractions.IFileParser;

public class ParquetFileParser implements IFileParser {

    public String getSupportedExtension() {
        return SupportingExtensionContants.PARQUET;
    }

    public AvroDataCarrier parseToAvro(String inputFilePath) throws YadConverterException {
        List<GenericRecord> avroRecords = new ArrayList<>();

        try {
            InputFile parquetFile = HadoopInputFile.fromPath(new Path(inputFilePath), new Configuration());
            try (ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(parquetFile).build()) {
                GenericRecord record;
                while ((record = reader.read()) != null) {
                    avroRecords.add(record);
                }
            }
        } catch (IOException ex) {
            throw new YadConverterException("Input file not found.");
        }

        return new AvroDataCarrier(avroRecords);
    }

}
