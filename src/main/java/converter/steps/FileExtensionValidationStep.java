package converter.steps;

import converter.abstractions.IConvertPilelineStep;
import converter.constants.SupportingExtensionContants;
import converter.exceptions.YadConverterException;
import converter.models.ConverterPipelineData;

public class FileExtensionValidationStep implements IConvertPilelineStep {
    private IConvertPilelineStep nextStep;

    public void setNextStep(IConvertPilelineStep nextStep) {
        this.nextStep = nextStep;
    }

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
