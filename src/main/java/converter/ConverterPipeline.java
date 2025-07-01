package converter;

import converter.abstractions.IConvertPilelineStep;
import converter.exceptions.YadConverterException;
import converter.models.ConverterPipelineData;
import converter.steps.*;

import utils.*;

/**
 * The {@code ConverterPipeline} class implements a chain-of-responsibility
 * pattern for file conversion steps. It allows sequential execution of multiple
 * conversion and validation steps, where each step processes the data and
 * passes it to the next step in the pipeline.
 * <p>
 * The pipeline can be configured by chaining steps using the
 * {@link #nextStep(Class)} method, and a default pipeline can be built using
 * {@link #buildDefault()}. The pipeline is executed by calling
 * {@link #run(ConverterPipelineData)}, which starts processing from the first
 * step.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>
 * ConverterPipeline pipeline = new ConverterPipeline().buildDefault();
 * pipeline.run(data);
 * </pre>
 *
 * @see IConvertPilelineStep
 * @see ConverterPipelineData
 * @see YadConverterException
 */
public class ConverterPipeline {
    private IConvertPilelineStep firstStep;
    private IConvertPilelineStep currentStep;

    /**
     * Builds and returns a default {@link ConverterPipeline} with a predefined sequence of processing steps.
     *
     * @return The default pipeline includes the following steps in order:
     * <ol>
     * <li>{@link FileExtensionValidationStep} - Validates the file extension.</li>
     * <li>{@link CsvToParquetConversionStep} - Converts CSV files to Parquet format.</li>
     * <li>{@link ParquetToCsvConversionStep} - Converts Parquet files back to CSV format.</li>
     * <li>{@link FallbackStep} - Handles any cases not covered by previous steps.</li>
     * </ol>
     */
    public ConverterPipeline buildDefault() {
        return this
            .nextStep(FileExtensionValidationStep.class)
            .nextStep(CsvToParquetConversionStep.class)
            .nextStep(ParquetToCsvConversionStep.class)
            .nextStep(FallbackStep.class);
    }

	/**
	 * Executes the converter pipeline starting from the first step using the provided data.
	 *
	 * @param data the data object containing information required for the conversion process
	 * @throws YadConverterException if an error occurs during the conversion pipeline execution
	 */
    public void run(ConverterPipelineData data) throws YadConverterException {
        firstStep.run(data);
    }

	/**
	 * Adds a new conversion step to the pipeline using the specified class type.
	 * The step is instantiated via {@link GenericTypeHelper#constructInstance(Class)}.
	 * If this is the first step, it initializes the pipeline; otherwise, it appends
	 * the step to the end of the current pipeline.
	 *
	 * @param <T>   the type of the conversion step, extending {@link IConvertPilelineStep}
	 * @param clazz the class of the conversion step to add
	 * @return      the current {@code ConverterPipeline} instance for method chaining
	 */
    public <T extends IConvertPilelineStep> ConverterPipeline nextStep(Class<T> clazz) {
        IConvertPilelineStep step = GenericTypeHelper.constructInstance(clazz);

        if (firstStep == null) {
            firstStep = currentStep = step;
        } else {
            currentStep.setNextStep(step);
            currentStep = step;
        }

        return this;
    }
}
