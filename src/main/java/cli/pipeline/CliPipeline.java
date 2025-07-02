package cli.pipeline;

import cli.pipeline.abstractions.ICliPipelineStep;
import cli.pipeline.models.CliPipelineData;
import cli.pipeline.steps.*;

/**
 * Represents a command-line interface (CLI) pipeline that processes a sequence of steps.
 * <p>
 * The pipeline is composed of several {@link ICliPipelineStep} implementations, each responsible
 * for a specific part of the CLI workflow, such as setting up options, parsing arguments,
 * executing commands, and finalizing the process.
 * </p>
 *
 * <p>
 * Usage:
 * <pre>
 *     new CliPipeline().run(args);
 * </pre>
 * </p>
 *
 * <p>
 * The pipeline steps are chained together in the following order:
 * <ol>
 *     <li>CliOptionsSetupStep</li>
 *     <li>CliOptionsParsingStep</li>
 *     <li>HelpCommandExecutionStep</li>
 *     <li>ConvertionCommandExecutionStep</li>
 *     <li>SayGoodbyeStep</li>
 * </ol>
 * </p>
 */
public class CliPipeline {
    private ICliPipelineStep firstStep;

    public CliPipeline() {
        ICliPipelineStep cliOptionsSetupStep = new CliOptionsSetupStep();
        ICliPipelineStep cliOptionsParsingStep = new CliOptionsParsingStep();
        ICliPipelineStep helpCommandExecutionStep = new HelpCommandExecutionStep();
        ICliPipelineStep convertionCommandExecutionStep = new ConvertionCommandExecutionStep();
        ICliPipelineStep sayGoodbyeStep = new SayGoodbyeStep();

        cliOptionsSetupStep
            .then(cliOptionsParsingStep)
            .then(helpCommandExecutionStep)
            .then(convertionCommandExecutionStep)
            .then(sayGoodbyeStep);

        firstStep = cliOptionsSetupStep;
    }

    public void run(String[] args) throws Exception {
        CliPipelineData data = new CliPipelineData(args);
        firstStep.run(data);
    }
}
