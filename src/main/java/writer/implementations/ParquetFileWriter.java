package writer.implementations;

import java.io.IOException;

import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.apache.parquet.io.OutputFile;

import common.constants.SupportingExtensionContants;
import common.exceptions.YadConverterException;
import common.models.AvroDataCarrier;

import writer.abstractions.IFileWriter;

public class ParquetFileWriter implements IFileWriter {

    @Override
    public String getSupportedExtension() {
        return SupportingExtensionContants.PARQUET;
    }

    @Override
    public void writeFromAvro(String outputFilePath, AvroDataCarrier data) throws YadConverterException {
        try {
            OutputFile parquetFile = HadoopOutputFile.fromPath(new Path(outputFilePath), new Configuration());

            try (ParquetWriter<GenericRecord> writer = AvroParquetWriter.<GenericRecord>builder(parquetFile)
                .withSchema(data.getSchema())
                .withCompressionCodec(CompressionCodecName.SNAPPY) // Snappy for good balance of speed/compression
                .build()) {
    
                for (GenericRecord avroRecord : data.getRecords()) {
                    writer.write(avroRecord);
                }
    
    
            }
        } catch (IOException ex) {
            throw new YadConverterException("Output file already exists.");
        }
    }

}
