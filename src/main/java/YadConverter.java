import cli.pipeline.CliPipeline;
import common.exceptions.YadConverterException;

public class YadConverter {

    public static void main(String[] args) {
        try {
            new CliPipeline()
                .run(args);
        } catch (YadConverterException ex) {
            System.err.printf("*** YadConverter error: %s", ex.getMessage());
        } catch (Exception ex) {
            System.err.println("*** Error: There's something went wrong, please try again.");
        }
	}

}
