package converter.steps;

import converter.abstractions.ConverterPipelineStepBase;
import converter.exceptions.YadConverterException;
import converter.models.ConverterPipelineData;

public class FallbackStep extends ConverterPipelineStepBase {
    public void run(ConverterPipelineData data) throws YadConverterException {
        throw new YadConverterException(
            "%s to %s conversion is not supported.",
            data.getInputFileExtension(),
            data.getOutputFileExtension());
    }
}
