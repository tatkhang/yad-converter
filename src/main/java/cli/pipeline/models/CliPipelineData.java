package cli.pipeline.models;

import org.apache.commons.cli.*;

public class CliPipelineData {
	private String[] cliArguments;

	private Options options;
	
	private boolean shouldShowHelp;
	private String inputFilePath;
	private String outputFilePath;
	
	public CliPipelineData(String[] args) {
		this.cliArguments = args;
	}

	public String[] getCliArguments() {
		return cliArguments;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public boolean shouldShowHelp() {
		return shouldShowHelp;
	}

	public void setShouldShowHelp(boolean shouldShowHelp) {
		this.shouldShowHelp = shouldShowHelp;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
}
