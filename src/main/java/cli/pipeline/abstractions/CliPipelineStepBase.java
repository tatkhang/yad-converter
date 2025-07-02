package cli.pipeline.abstractions;

import cli.pipeline.models.CliPipelineData;

public abstract class CliPipelineStepBase implements ICliPipelineStep {
    private ICliPipelineStep nextStep;

    public ICliPipelineStep then(ICliPipelineStep nextStep) {
        return this.nextStep = nextStep;
    }

    public void tryRunNextStep(CliPipelineData data) throws Exception {
        if (nextStep == null) {
            return;
        }

        nextStep.run(data);
    }
}
