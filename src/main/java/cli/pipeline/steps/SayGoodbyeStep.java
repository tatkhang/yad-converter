package cli.pipeline.steps;

import cli.pipeline.abstractions.CliPipelineBaseStep;
import cli.pipeline.models.CliPipelineData;

public class SayGoodbyeStep extends CliPipelineBaseStep {

	public void run(CliPipelineData data) {
		System.out.println("---------------------------------------");
		System.out.println("|  Thank you for using YadConverter!  |");
		System.out.println("|  Have a nice day :)                 |");
		System.out.println("---------------------------------------");
	}
}
