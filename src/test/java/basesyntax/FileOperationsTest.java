package basesyntax;

import basesyntax.service.FileReader;
import basesyntax.service.FileWriter;
import basesyntax.serviceimpl.FileReaderImpl;
import basesyntax.serviceimpl.FileWriterImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("File I/O Operations")
class FileOperationsTest {

    @TempDir
    public static Path tempDir;

    private FileReader fileReader;
    private FileWriter fileWriter;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
        fileWriter = new FileWriterImpl();
    }

    @Test
    @DisplayName("FileReaderImpl should read lines from a file correctly")
    void read_shouldReturnCorrectLines() throws IOException {
        Path tempFile = tempDir.resolve("test_input.txt");
        Files.write(tempFile, List.of("line1", "line2", "line3"));
        List<String> lines = fileReader.read(tempFile.toString());
        Assertions.assertEquals(List.of("line1", "line2", "line3"), lines);
    }

    @Test
    @DisplayName("FileReaderImpl should throw RuntimeException for non-existent file")
    void read_nonExistentFile_shouldThrowException() {
        Assertions.assertThrows(RuntimeException.class, () ->
                fileReader.read("non_existent_file.txt"));
    }

    @Test
    @DisplayName("FileWriterImpl should write data to a file correctly")
    void write_shouldWriteCorrectData() throws IOException {
        Path tempFile = tempDir.resolve("test_output.txt");
        String data = "Hello, world!";
        fileWriter.write(data, tempFile.toString());
        String writtenData = Files.readAllLines(tempFile).stream()
                .collect(Collectors.joining(System.lineSeparator()));
        Assertions.assertEquals(data, writtenData);
    }
}