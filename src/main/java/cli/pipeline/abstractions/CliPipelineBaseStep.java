package cli.pipeline.abstractions;

public abstract class CliPipelineBaseStep implements ICliPipelineStep {
	protected ICliPipelineStep nextStep;

	public void setNextStep(ICliPipelineStep nextStep) {
		this.nextStep = nextStep;		
	}
}
