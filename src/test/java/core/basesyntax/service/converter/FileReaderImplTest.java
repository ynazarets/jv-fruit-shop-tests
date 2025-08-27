package core.basesyntax.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.reader.FileReaderImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileReaderImplTest {
    private FileReaderImpl fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
    }

    @Test
    void shouldReadExistingFileCorrectly(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");
        List<String> expectedContent = List.of("line1", "line2", "line3");
        Files.write(file, expectedContent);

        List<String> actualContent = fileReader.read(file.toString());
        assertEquals(expectedContent, actualContent);
    }

    @Test
    void shouldThrowExceptionForNonExistentFile(@TempDir Path tempDir) {
        Path file = tempDir.resolve("non_existent_file.txt");
        assertThrows(RuntimeException.class,
                () -> fileReader.read(file.toString()));
    }

    @Test
    void shouldReadEmptyFileCorrectly(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.write(file, Collections.emptyList());

        List<String> actualContent = fileReader.read(file.toString());
        assertEquals(0, actualContent.size());
    }
}
