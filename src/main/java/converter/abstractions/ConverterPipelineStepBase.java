package converter.abstractions;

public abstract class ConverterPipelineStepBase implements IConvertPilelineStep {
    protected IConvertPilelineStep nextStep;

    public IConvertPilelineStep then(IConvertPilelineStep nextStep) {
        return this.nextStep = nextStep;
    }
}
