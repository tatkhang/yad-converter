package converter.steps;

import common.constants.SupportingExtensionContants;
import common.exceptions.YadConverterException;
import converter.abstractions.ConverterPipelineStepBase;
import converter.models.ConverterPipelineData;

public class FileExtensionValidationStep extends ConverterPipelineStepBase {

    public void run(ConverterPipelineData data) throws YadConverterException {
        String inputFileExtension = data.getInputFileExtension();
        String outputFileExtension = data.getOutputFileExtension();

        if (!SupportingExtensionContants.isValidFileExtension(inputFileExtension)) {
            throw new YadConverterException("Input file extension is not supported.");
        }

        if (!SupportingExtensionContants.isValidFileExtension(outputFileExtension)) {
            throw new YadConverterException("Output file extension is not supported.");
        }

        if (inputFileExtension.equalsIgnoreCase(outputFileExtension)) {
            throw new YadConverterException("Input file extension and Output file extension cannot be the same.");
        }

        nextStep.run(data);
    }

}
