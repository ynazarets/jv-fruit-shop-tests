package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.service.FileWriter;
import core.basesyntax.serviceimpl.FileWriterImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("FileWriter Operations")
public class FileWriterTest {

    @TempDir
    private static Path tempDir;
    private FileWriter fileWriter;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriterImpl();
    }

    @Test
    @DisplayName("FileWriterImpl should write data to a file correctly")
    void write_shouldWriteCorrectData() throws IOException {
        Path tempFile = tempDir.resolve("test_output.txt");
        String data = "Hello, world!";
        fileWriter.write(data, tempFile.toString());
        String writtenData = Files.readAllLines(tempFile).stream()
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(data, writtenData);
    }
}
