package basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import basesyntax.common.FruitTransaction;
import basesyntax.service.DataConverter;
import basesyntax.serviceimpl.DataConverterImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DataConverterImpl")
class DataConverterImplTest {

    private DataConverter dataConverter;

    @BeforeEach
    void setUp() {
        dataConverter = new DataConverterImpl();
    }

    @Test
    @DisplayName("should convert valid raw data to list of FruitTransaction objects")
    void convertToTransaction_validData_shouldReturnCorrectList() {
        List<String> rawData = List.of("type,fruit,quantity", "s,banana,10", "p,apple,5");
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawData);

        assertEquals(2, transactions.size());
        assertEquals(FruitTransaction.Operation.SUPPLY,
                transactions.get(0).getOperation());
        assertEquals("banana", transactions.get(0).getFruit());
        assertEquals(10, transactions.get(0).getQuantity());
        assertEquals(FruitTransaction.Operation.PURCHASE,
                transactions.get(1).getOperation());
        assertEquals("apple", transactions.get(1).getFruit());
        assertEquals(5, transactions.get(1).getQuantity());
    }

    @Test
    @DisplayName("should throw RuntimeException for invalid number of columns")
    void convertToTransaction_invalidColumnCount_shouldThrowException() {
        List<String> rawData = List.of("type,fruit,quantity", "s,banana,10,extra");
        assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransaction(rawData));
    }

    @Test
    @DisplayName("should throw RuntimeException for invalid quantity format")
    void convertToTransaction_invalidQuantityFormat_shouldThrowException() {
        List<String> rawData = List.of("type,fruit,quantity", "s,banana,ten");
        assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransaction(rawData));
    }

    @Test
    @DisplayName("should throw RuntimeException for empty fruit name")
    void convertToTransaction_emptyFruitName_shouldThrowException() {
        List<String> rawData = List.of("type,fruit,quantity", "s,,10");
        assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransaction(rawData));
    }

    @Test
    @DisplayName("should throw RuntimeException for negative quantity")
    void convertToTransaction_negativeQuantity_shouldThrowException() {
        List<String> rawData = List.of("type,fruit,quantity", "p,banana,-10");
        assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransaction(rawData));
    }
}
