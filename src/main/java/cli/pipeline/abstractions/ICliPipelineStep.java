package cli.pipeline.abstractions;

import cli.pipeline.models.CliPipelineData;

public interface ICliPipelineStep {
	void setNextStep(ICliPipelineStep nextStep);
	void run(CliPipelineData data) throws Exception;
}
