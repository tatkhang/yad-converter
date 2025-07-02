package converter.abstractions;

import common.exceptions.YadConverterException;
import converter.models.ConverterPipelineData;

public abstract class ConverterPipelineStepBase implements IConvertPilelineStep {
    private IConvertPilelineStep nextStep;

    public IConvertPilelineStep then(IConvertPilelineStep nextStep) {
        return this.nextStep = nextStep;
    }

    public void tryRunNextStep(ConverterPipelineData data) throws YadConverterException {
        if (nextStep == null) {
            return;
        }

        nextStep.run(data);
    }
}
