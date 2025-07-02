package cli.pipeline.abstractions;

public abstract class CliPipelineStepBase implements ICliPipelineStep {
    protected ICliPipelineStep nextStep;

    public ICliPipelineStep then(ICliPipelineStep nextStep) {
        return this.nextStep = nextStep;
    }
}
