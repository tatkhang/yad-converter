package parser;

import java.util.HashMap;
import java.util.Map;

import common.exceptions.YadConverterException;
import common.utils.GenericTypeHelper;

import parser.abstractions.IFileParser;
import parser.implementations.*;

public class FileParserRegistry {
    private final Map<String, IFileParser> parsers;

    public FileParserRegistry() {
        parsers = new HashMap<String, IFileParser>();
    }

    public static FileParserRegistry buildDefault() {
        return new FileParserRegistry()
            .register(CsvFileParser.class)
            .register(ParquetFileParser.class);
    }

    public <T extends IFileParser> FileParserRegistry register(Class<T> clazz) {
        IFileParser parser = GenericTypeHelper.constructInstance(clazz);

        return register(parser);
    }

    public FileParserRegistry register(IFileParser parser) {
        parsers.putIfAbsent(parser.getSupportedExtension(), parser);

        return this;
    }

    public IFileParser get(String fileExtension) throws YadConverterException {
        if (!parsers.containsKey(fileExtension)) {
            throw new YadConverterException("Parser for type '%s' is not registered.", fileExtension);
        }

        return parsers.get(fileExtension);
    }
}
