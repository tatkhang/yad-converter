package converter.abstractions;

import converter.exceptions.YadConverterException;
import converter.models.ConverterPipelineData;

/**
 * Represents a step in a converter pipeline.
 * Each step can be linked to the next step, forming a chain of responsibility.
 */
public interface IConvertPilelineStep {
    /**
     * Sets the next step in the conversion pipeline.
     *
     * @param nextStep the next {@code IConvertPilelineStep} to be executed after this step
     */
    void setNextStep(IConvertPilelineStep nextStep);

    /**
     * Executes the pipeline step using the provided {@link ConverterPipelineData}.
     *
     * @param data the data object containing information required for the conversion step
     * @throws YadConverterException if an error occurs during the execution of the pipeline step
     */
    void run(ConverterPipelineData data) throws YadConverterException;
}
