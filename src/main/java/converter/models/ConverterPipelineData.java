package converter.models;

import common.models.AvroDataCarrier;
import converter.constants.FileConstants;

public class ConverterPipelineData {
    private String inputFilePath;
    private String outputFilePath;

    private String inputFileExtension;
    private String outputFileExtension;

    private AvroDataCarrier avroData;

    public ConverterPipelineData(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.inputFileExtension = extractFileExtension(inputFilePath);

        this.outputFilePath = outputFilePath;
        this.outputFileExtension = extractFileExtension(outputFilePath);
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getInputFileExtension() {
        return inputFileExtension;
    }

    public String getOutputFileExtension() {
        return outputFileExtension;
    }

    public AvroDataCarrier getAvroData() {
        return avroData;
    }

    public void setAvroData(AvroDataCarrier avroData) {
        this.avroData = avroData;
    }

    private String extractFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        int delimiterIndex = filePath.lastIndexOf(FileConstants.FILE_EXTENSION_DELIMITER);
        boolean isDelimiterFirstCharacter = delimiterIndex <= 0;
        boolean isDelimiterLastCharacter = delimiterIndex >= filePath.length() - 1;

        if (isDelimiterFirstCharacter || isDelimiterLastCharacter) {
            return "";
        }

        return filePath.substring(delimiterIndex + 1).toLowerCase();
    }
}
