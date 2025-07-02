package cli.pipeline.steps;

import cli.pipeline.abstractions.CliPipelineStepBase;
import cli.pipeline.models.CliPipelineData;
import converter.ConverterPipeline;
import converter.models.ConverterPipelineData;

public class ConvertionCommandExecutionStep extends CliPipelineStepBase {

    public void run(CliPipelineData data) throws Exception {
        ConverterPipelineData converterPipelineData = new ConverterPipelineData(
            data.getInputFilePath(),
            data.getOutputFilePath());

        ConverterPipeline
            .buildDefault()
            .run(converterPipelineData);

        System.out.println(
            String.format("Successfully converted. The results are available at '%s'", data.getOutputFilePath()));

        nextStep.run(data);
    }

}
