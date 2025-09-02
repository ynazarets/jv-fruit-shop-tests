package core.basesyntax;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;
import core.basesyntax.service.DataConverter;
import core.basesyntax.service.FileReader;
import core.basesyntax.service.FileWriter;
import core.basesyntax.service.OperationStrategy;
import core.basesyntax.service.ReportGenerator;
import core.basesyntax.service.ShopService;
import core.basesyntax.serviceimpl.DataConverterImpl;
import core.basesyntax.serviceimpl.FileReaderImpl;
import core.basesyntax.serviceimpl.FileWriterImpl;
import core.basesyntax.serviceimpl.OperationStrategyImpl;
import core.basesyntax.serviceimpl.ReportGeneratorImpl;
import core.basesyntax.serviceimpl.ShopServiceImpl;
import core.basesyntax.strategy.BalanceOperation;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.PurchaseOperation;
import core.basesyntax.strategy.ReturnOperation;
import core.basesyntax.strategy.SupplyOperation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("ShopService")
class ShopServiceIntegrationTest {

    @TempDir
    private Path tempDir;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private DataConverter dataConverter;
    private ShopService shopService;
    private ReportGenerator reportGenerator;
    private Path inputFilePath;
    private Path outputFilePath;

    @BeforeEach
    void setUp() throws IOException {
        fileReader = new FileReaderImpl();
        fileWriter = new FileWriterImpl();
        dataConverter = new DataConverterImpl();
        Map<FruitTransaction.Operation, OperationHandler> handlerMap = new HashMap<>();
        handlerMap.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        handlerMap.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        handlerMap.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        handlerMap.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(handlerMap);
        shopService = new ShopServiceImpl(operationStrategy);
        reportGenerator = new ReportGeneratorImpl();
        inputFilePath = tempDir.resolve("input.csv");
        outputFilePath = tempDir.resolve("report.csv");
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    @DisplayName("should correctly process transactions and generate a valid report")
    void testFullProcess_validData() throws IOException {
        List<String> rawData = List.of(
                "type,fruit,quantity",
                "b,banana,100",
                "s,apple,50",
                "s,banana,20",
                "p,banana,30",
                "r,apple,5",
                "b,orange,45"
        );
        Files.write(inputFilePath, rawData);
        List<String> rawTransactions = fileReader.read(inputFilePath.toString());
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawTransactions);
        shopService.process(transactions);
        String report = reportGenerator.getReport();
        fileWriter.write(report, outputFilePath.toString());
        String expectedReport = "fruit,quantity" + lineSeparator()
                + "apple,55" + lineSeparator()
                + "banana,90" + lineSeparator()
                + "orange,45" + lineSeparator();
        String generatedReport = Files.readAllLines(outputFilePath).stream()
                .collect(StringBuilder::new, (sb, s) -> sb.append(s)
                        .append(lineSeparator()), StringBuilder::append)
                .toString();
        assertEquals(expectedReport.split(lineSeparator()).length,
                generatedReport.split(lineSeparator()).length);
        for (String line : expectedReport.split(lineSeparator())) {
            assert (generatedReport.contains(line));
        }
    }

    @Test
    @DisplayName("should generate a correct report for an empty input file (with header)")
    void testFullProcess_emptyInputWithHeader() throws IOException {
        List<String> rawData = List.of("type,fruit,quantity");
        Files.write(inputFilePath, rawData);
        List<String> rawTransactions = fileReader.read(inputFilePath.toString());
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawTransactions);
        shopService.process(transactions);
        String report = reportGenerator.getReport();
        fileWriter.write(report, outputFilePath.toString());
        String expectedReport = "fruit,quantity" + lineSeparator();
        String generatedReport = Files.readString(outputFilePath);
        assertEquals(expectedReport, generatedReport);
    }

    @Test
    @DisplayName("should generate a correct report for a truly empty file (no header)")
    void testFullProcess_trulyEmptyInput() throws IOException {
        List<String> rawData = List.of();
        Files.write(inputFilePath, rawData);
        List<String> rawTransactions = fileReader.read(inputFilePath.toString());
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawTransactions);
        shopService.process(transactions);
        String report = reportGenerator.getReport();
        fileWriter.write(report, outputFilePath.toString());
        String expectedReport = "fruit,quantity" + lineSeparator();
        String generatedReport = Files.readString(outputFilePath);
        assertEquals(expectedReport, generatedReport);
    }

    @Test
    @DisplayName("should process a single transaction correctly")
    void testFullProcess_singleTransaction() throws IOException {
        List<String> rawData = List.of("type,fruit,quantity", "s,banana,100");
        Files.write(inputFilePath, rawData);
        List<String> rawTransactions = fileReader.read(inputFilePath.toString());
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawTransactions);
        shopService.process(transactions);
        String report = reportGenerator.getReport();
        fileWriter.write(report, outputFilePath.toString());
        String expectedReport = "fruit,quantity" + lineSeparator() + "banana,100" + lineSeparator();
        String generatedReport = Files.readString(outputFilePath);
        assertEquals(expectedReport, generatedReport);
    }

    @Test
    @DisplayName("should throw RuntimeException for invalid purchase (insufficient stock)")
    void testFullProcess_invalidPurchase() throws IOException {
        List<String> rawData = List.of(
                "type,fruit,quantity",
                "b,banana,10",
                "p,banana,20"
        );
        Files.write(inputFilePath, rawData);
        List<String> rawTransactions = fileReader.read(inputFilePath.toString());
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawTransactions);
        assertThrows(RuntimeException.class, () -> shopService.process(transactions));
    }

    @Test
    @DisplayName("should throw RuntimeException for invalid data format (non-numeric quantity)")
    void testFullProcess_invalidQuantityFormat() throws IOException {
        List<String> rawData = List.of(
                "type,fruit,quantity",
                "s,apple,ten"
        );
        Files.write(inputFilePath, rawData);
        List<String> rawTransactions = fileReader.read(inputFilePath.toString());
        assertThrows(RuntimeException.class, () -> dataConverter
                .convertToTransaction(rawTransactions));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for unknown operation code")
    void testFullProcess_unknownOperation() throws IOException {
        List<String> rawData = List.of(
                "type,fruit,quantity",
                "x,orange,50"
        );
        Files.write(inputFilePath, rawData);
        List<String> rawTransactions = fileReader.read(inputFilePath.toString());
        assertThrows(IllegalArgumentException.class, () -> dataConverter
                .convertToTransaction(rawTransactions));
    }
}
