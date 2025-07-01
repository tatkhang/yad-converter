package cli.pipeline.steps;

import cli.pipeline.abstractions.CliPipelineBaseStep;
import cli.pipeline.models.CliPipelineData;
import converter.ConverterPipeline;
import converter.models.ConverterPipelineData;

public class ConvertionCommandExecutionStep extends CliPipelineBaseStep {

    public void run(CliPipelineData data) throws Exception {
        ConverterPipelineData converterPipelineData = new ConverterPipelineData(
            data.getInputFilePath(),
            data.getOutputFilePath());

        new ConverterPipeline()
            .buildDefault()
            .run(converterPipelineData);

        System.out.println(
            String.format("Conversion successful. Please find your output in %s", data.getOutputFilePath()));

        nextStep.run(data);
    }

}
