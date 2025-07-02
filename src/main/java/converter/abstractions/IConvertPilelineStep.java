package converter.abstractions;

import common.exceptions.YadConverterException;
import converter.models.ConverterPipelineData;

/**
 * Represents a step in a converter pipeline.
 * Each step can be chained with another step and executed with pipeline data.
 */
public interface IConvertPilelineStep {
    /**
     * Chains the current pipeline step with the specified next step.
     *
     * @param nextStep the next {@code IConvertPilelineStep} to execute after the current step
     * @return the combined {@code IConvertPilelineStep} representing the chained steps
     */
    IConvertPilelineStep then(IConvertPilelineStep nextStep);

    /**
     * Executes the pipeline step using the provided {@link ConverterPipelineData}.
     *
     * @param data the data object containing information required for the conversion step
     * @throws YadConverterException if an error occurs during the execution of the pipeline step
     */
    void run(ConverterPipelineData data) throws YadConverterException;
}
