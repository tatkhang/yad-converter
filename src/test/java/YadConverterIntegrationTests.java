import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class YadConverterIntegrationTests {
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    void testMainWithHelpArgs() {
        String[] args = {"--help"};
        
        assertDoesNotThrow(() -> YadConverter.main(args));

        String output = errorStreamCaptor.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains("*** YadConverter error:"));
        assertFalse(output.contains("something went wrong"));
    }

    @Test
    void testMainWithInvalidArgs() throws Exception {
        String[] args = {"-i", "cause-yad-converter.exception", "-o", "cause-yad-converter.exception"};

        YadConverter.main(args);

        String output = errorStreamCaptor.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("YadConverter error:") || output.contains("something went wrong"));
    }

    @ParameterizedTest
    @CsvSource({"sample.csv,sample.parquet", "sample.parquet,sample.csv"})
    void testCsvToParquetConversion(String inputFileName, String outputFileName) throws IOException {

        // Arrange: copy input file to a temp file
        Path tempDir = Files.createTempDirectory("yadconverter-test");
        Path inputFile = tempDir.resolve(inputFileName);
        Path outputFile = tempDir.resolve(outputFileName);

        try (InputStream in = getClass().getResourceAsStream("/" + inputFileName)) {
            assertNotNull(in, "Test resource " + inputFileName + " not found in src/test/resources");
            Files.copy(in, inputFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // Act: run the converter
        String[] args = {"-i", inputFile.toString(), "-o", outputFile.toString()};
        assertDoesNotThrow(() -> YadConverter.main(args));

        String output = errorStreamCaptor.toString(StandardCharsets.UTF_8);
        // Assert: output file exists and is not empty
        assertTrue(Files.exists(outputFile), "Output " + outputFileName + " file was not created");
        assertTrue(Files.size(outputFile) > 0, "Output " + outputFileName + " file is empty");
    }
}