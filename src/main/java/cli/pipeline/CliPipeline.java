package cli.pipeline;

import cli.pipeline.abstractions.ICliPipelineStep;
import cli.pipeline.models.CliPipelineData;
import cli.pipeline.steps.*;

public class CliPipeline {
    private ICliPipelineStep firstStep;

    public CliPipeline() {
        ICliPipelineStep cliOptionsSetupStep = new CliOptionsSetupStep();
        ICliPipelineStep cliOptionsParsingStep = new CliOptionsParsingStep();
        ICliPipelineStep helpCommandExecutionStep = new HelpCommandExecutionStep();
        ICliPipelineStep convertionCommandExecutionStep = new ConvertionCommandExecutionStep();
        ICliPipelineStep sayGoodbyeStep = new SayGoodbyeStep();

        cliOptionsSetupStep.setNextStep(cliOptionsParsingStep);
        cliOptionsParsingStep.setNextStep(helpCommandExecutionStep);
        helpCommandExecutionStep.setNextStep(convertionCommandExecutionStep);
        convertionCommandExecutionStep.setNextStep(sayGoodbyeStep);

        firstStep = cliOptionsSetupStep;
    }

    public void run(String[] args) throws Exception {
        CliPipelineData data = new CliPipelineData(args);
        firstStep.run(data);
    }
}
