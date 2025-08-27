package core.basesyntax.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.service.report.ReportGeneratorImpl;
import core.basesyntax.storage.Storage;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private ReportGeneratorImpl reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
    }

    @AfterEach
    void tearDown() {
        Map<String, Integer> fruits = Storage.getFruits();
        fruits.clear();
    }

    @Test
    void shouldGenerateReportCorrectlyWithMultipleFruits() {
        Storage.put("apple", 100);
        Storage.put("banana", 50);
        Storage.put("orange", 75);

        String expectedReport = "fruit,quantity" + System.lineSeparator()
                + "apple,100" + System.lineSeparator()
                + "banana,50" + System.lineSeparator()
                + "orange,75" + System.lineSeparator();

        String actualReport = reportGenerator.getReport();
        assertEquals(expectedReport, actualReport);
    }
}
