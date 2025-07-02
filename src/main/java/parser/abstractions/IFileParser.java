package parser.abstractions;

import common.exceptions.YadConverterException;
import common.models.AvroDataCarrier;

public interface IFileParser {
    String getSupportedExtension();
    AvroDataCarrier parseToAvro(String inputFilePath) throws YadConverterException;
}
