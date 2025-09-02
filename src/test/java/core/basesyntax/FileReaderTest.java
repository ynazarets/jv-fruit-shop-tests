package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.FileReader;
import core.basesyntax.serviceimpl.FileReaderImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("FileReader Operations")
class FileReaderTest {

    @TempDir
    private static Path tempDir;
    private FileReader fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(file -> {
                    if (file.isFile()) {
                        file.delete();
                    }
                });
    }

    @Test
    @DisplayName("FileReaderImpl should read lines from a file correctly")
    void read_shouldReturnCorrectLines() throws IOException {
        Path tempFile = tempDir.resolve("test_input.txt");
        Files.write(tempFile, List.of("line1", "line2", "line3"));
        List<String> lines = fileReader.read(tempFile.toString());
        assertEquals(List.of("line1", "line2", "line3"), lines);
    }

    @Test
    @DisplayName("FileReaderImpl should throw RuntimeException for non-existent file")
    void read_nonExistentFile_shouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                fileReader.read("non_existent_file.txt"));
    }
}
