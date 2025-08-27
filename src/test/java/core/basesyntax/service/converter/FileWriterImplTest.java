package core.basesyntax.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.writer.FileWriterImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileWriterImplTest {
    private FileWriterImpl fileWriter;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriterImpl();
    }

    @Test
    void shouldWriteToFileCorrectly(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("output.txt");
        String data = "fruit,quantity\napple,100\nbanana,50";

        fileWriter.write(data, file.toString());

        String writtenContent = Files.readString(file);
        assertEquals(data, writtenContent);
    }

    @Test
    void shouldThrowExceptionIfFileCannotBeWrittenTo(@TempDir Path tempDir) {
        Path nonWritableDir = tempDir.resolve("non_writable_dir");
        File nonWritableFile = new File(nonWritableDir.toFile(), "test.txt");
        nonWritableDir.toFile().setWritable(false);

        assertThrows(RuntimeException.class,
                () -> fileWriter.write("test data", nonWritableFile.toString()));
    }
}
