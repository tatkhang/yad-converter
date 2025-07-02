package writer;

import java.util.HashMap;
import java.util.Map;

import common.exceptions.YadConverterException;
import common.utils.GenericTypeHelper;

import writer.abstractions.IFileWriter;
import writer.implementations.*;

public class FileWriterRegistry {
    private final Map<String, IFileWriter> writers;

    public FileWriterRegistry() {
        writers = new HashMap<String, IFileWriter>();
    }

    public static FileWriterRegistry buildDefault() {
        return new FileWriterRegistry()
            .register(ParquetFileWriter.class)
            .register(CsvFileWriter.class);
    }

    public <T extends IFileWriter> FileWriterRegistry register(Class<T> clazz) {
        IFileWriter writer = GenericTypeHelper.constructInstance(clazz);

        return register(writer);
    }

    public FileWriterRegistry register(IFileWriter writer) {
        writers.putIfAbsent(writer.getSupportedExtension(), writer);

        return this;
    }

    public IFileWriter get(String fileExtension) throws YadConverterException {
        if (!writers.containsKey(fileExtension)) {
            throw new YadConverterException("Writer for type '%s' is not registered.", fileExtension);
        }

        return writers.get(fileExtension);
    }
}
