package core.basesyntax;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.db.Storage;
import core.basesyntax.service.ReportGenerator;
import core.basesyntax.serviceimpl.ReportGeneratorImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ReportGeneratorImpl")
class ReportGeneratorImplTest {
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    @DisplayName("should generate a report with correct header and content")
    void getReport_withData_shouldGenerateCorrectReport() {
        Storage.put("apple", 100);
        Storage.put("banana", 50);
        String expectedReport = "fruit,quantity" + lineSeparator()
                + "apple,100" + lineSeparator()
                + "banana,50" + lineSeparator();
        assertEquals(expectedReport, reportGenerator.getReport());
    }

    @Test
    @DisplayName("should generate a report with only header if storage is empty")
    void getReport_emptyStorage_shouldGenerateOnlyHeader() {
        String expectedReport = "fruit,quantity" + lineSeparator();
        assertEquals(expectedReport, reportGenerator.getReport());
    }
}
