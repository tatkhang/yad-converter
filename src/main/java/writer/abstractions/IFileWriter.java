package writer.abstractions;

import common.exceptions.YadConverterException;
import common.models.AvroDataCarrier;

public interface IFileWriter {
    String getSupportedExtension();
    void writeFromAvro(String outputFilePath, AvroDataCarrier data) throws YadConverterException;
}
