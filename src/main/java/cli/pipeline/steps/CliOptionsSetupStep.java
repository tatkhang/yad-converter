package cli.pipeline.steps;

import org.apache.commons.cli.*;

import cli.pipeline.abstractions.CliPipelineStepBase;
import cli.pipeline.constants.OptionConstants;
import cli.pipeline.models.CliPipelineData;

public class CliOptionsSetupStep extends CliPipelineStepBase {

    public void run(CliPipelineData data) throws Exception {
        Options options = new Options();

        options.addOption(
            Option
                .builder(OptionConstants.HELP)
                .longOpt("help")
                .desc("Display help information")
                .build());
        options.addOption(
            Option
                .builder(OptionConstants.INPUT)
                .longOpt("input")
                .argName("file")
                .hasArg()
                .required()
                .desc("Input file path (e.g., source.csv or source.parquet)")
                .build());
        options.addOption(
            Option
                .builder(OptionConstants.OUTPUT)
                .longOpt("output")
                .argName("file")
                .hasArg()
                .required()
                .desc("Output file path (e.g., data.parquet or data.csv)")
                .build());

        data.setOptions(options);

        tryRunNextStep(data);
    }

}
