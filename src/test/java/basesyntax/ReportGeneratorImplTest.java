package basesyntax;

import basesyntax.db.Storage;
import basesyntax.service.ReportGenerator;
import basesyntax.serviceimpl.ReportGeneratorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ReportGeneratorImpl")
class ReportGeneratorImplTest {
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
        Storage.clear();
    }

    @Test
    @DisplayName("should generate a report with correct header and content")
    void getReport_withData_shouldGenerateCorrectReport() {
        Storage.put("apple", 100);
        Storage.put("banana", 50);
        String expectedReport = "fruit,quantity" + System.lineSeparator()
                + "apple,100" + System.lineSeparator()
                + "banana,50" + System.lineSeparator();
        Assertions.assertEquals(expectedReport, reportGenerator.getReport());
    }

    @Test
    @DisplayName("should generate a report with only header if storage is empty")
    void getReport_emptyStorage_shouldGenerateOnlyHeader() {
        String expectedReport = "fruit,quantity" + System.lineSeparator();
        Assertions.assertEquals(expectedReport, reportGenerator.getReport());
    }
}
