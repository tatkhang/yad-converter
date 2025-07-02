package cli.pipeline.steps;

import org.apache.commons.cli.*;

import cli.pipeline.abstractions.CliPipelineStepBase;
import cli.pipeline.constants.OptionConstants;
import cli.pipeline.models.CliPipelineData;

public class CliOptionsParsingStep extends CliPipelineStepBase {

    public void run(CliPipelineData data) throws Exception {
        CommandLineParser parser = new DefaultParser();
        String[] args = data.getCliArguments();
        Options options = data.getOptions();

        boolean shouldShowHelp = false;
        String inputFilePath = null;
        String outputFilePath = null;

        try {
            CommandLine cmd = parser.parse(options, args);

            shouldShowHelp = cmd.hasOption(OptionConstants.HELP) || args.length == 0;
            inputFilePath = cmd.getOptionValue(OptionConstants.INPUT);
            outputFilePath = cmd.getOptionValue(OptionConstants.OUTPUT);
        } catch (ParseException ex) {
            shouldShowHelp = true;
        }

        data.setShouldShowHelp(shouldShowHelp);
        data.setInputFilePath(inputFilePath);
        data.setOutputFilePath(outputFilePath);

        nextStep.run(data);
    }

}
