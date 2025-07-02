package converter.steps;

import common.exceptions.YadConverterException;

import converter.abstractions.ConverterPipelineStepBase;
import converter.models.ConverterPipelineData;

import writer.FileWriterRegistry;
import writer.abstractions.IFileWriter;

public class OutputFileWritingStep extends ConverterPipelineStepBase {

    public void run(ConverterPipelineData data) throws YadConverterException {
        FileWriterRegistry registry = FileWriterRegistry.buildDefault();
        IFileWriter filewriter = registry.get(data.getOutputFileExtension());

        filewriter.writeFromAvro(data.getOutputFilePath(), data.getAvroData());
    }

}
