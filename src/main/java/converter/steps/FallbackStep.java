package converter.steps;

import converter.abstractions.IConvertPilelineStep;
import converter.exceptions.YadConverterException;
import converter.models.ConverterPipelineData;

public class FallbackStep implements IConvertPilelineStep {
    public void setNextStep(IConvertPilelineStep nextStep) {
    }

    public void run(ConverterPipelineData data) throws YadConverterException {
        throw new YadConverterException(
            "%s to %s conversion is not supported.",
            data.getInputFileExtension(),
            data.getOutputFileExtension());
    }
}
