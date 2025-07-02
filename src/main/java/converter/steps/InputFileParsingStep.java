package converter.steps;

import common.exceptions.YadConverterException;
import common.models.AvroDataCarrier;

import converter.abstractions.ConverterPipelineStepBase;
import converter.models.ConverterPipelineData;

import parser.FileParserRegistry;
import parser.abstractions.IFileParser;

public class InputFileParsingStep extends ConverterPipelineStepBase {

    public void run(ConverterPipelineData data) throws YadConverterException {
        FileParserRegistry registry = FileParserRegistry.buildDefault();
        IFileParser fileParser = registry.get(data.getInputFileExtension());

        AvroDataCarrier avroData = fileParser.parseToAvro(data.getInputFilePath());
        data.setAvroData(avroData);

        nextStep.run(data);
    }

}
