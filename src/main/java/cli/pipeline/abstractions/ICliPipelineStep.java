package cli.pipeline.abstractions;

import cli.pipeline.models.CliPipelineData;

/**
 * Represents a step in a CLI pipeline.
 * Each step can be chained with another step and executed with pipeline data.
 */
public interface ICliPipelineStep {
    /**
     * Chains the current pipeline step with the specified next step.
     *
     * @param nextStep the next {@code ICliPipelineStep} to execute after the current step
     * @return the combined {@code ICliPipelineStep} representing the chained steps
     */
    ICliPipelineStep then(ICliPipelineStep nextStep);

    /**
     * Executes the pipeline step using the provided {@link CliPipelineData}.
     *
     * @param data the data object containing context and information for the pipeline step
     * @throws Exception if an error occurs during execution of the pipeline step
     */
    void run(CliPipelineData data) throws Exception;
}
