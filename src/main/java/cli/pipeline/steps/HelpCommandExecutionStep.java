package cli.pipeline.steps;

import org.apache.commons.cli.*;

import cli.pipeline.abstractions.CliPipelineStepBase;
import cli.pipeline.models.CliPipelineData;

public class HelpCommandExecutionStep extends CliPipelineStepBase {

	public void run(CliPipelineData data) throws Exception {
		if (!data.shouldShowHelp()) {
			nextStep.run(data);
			return;
		}

		new HelpFormatter().printHelp("java -jar YadConverter.jar -i <file path> -o <file path>", data.getOptions());
	}
}
