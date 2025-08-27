package core.basesyntax.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.service.common.FruitTransaction;
import core.basesyntax.storage.Storage;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DataConverterImplTest {

    private DataConverterImpl dataConverter;

    @BeforeEach
    void setUp() {
        dataConverter = new DataConverterImpl();
        Storage.clear();
    }

    @Test
    void shouldConvertValidDataCorrectly() {
        List<String> rawData = List.of(
                "fruit,quantity",
                "b,banana,20",
                "s,apple,100",
                "p,banana,13",
                "r,apple,10"
        );
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawData);

        assertEquals(4, transactions.size());
        assertEquals(FruitTransaction.Operation.BALANCE, transactions.get(0).getOperation());
        assertEquals("banana", transactions.get(0).getFruit());
        assertEquals(20, transactions.get(0).getQuantity());

        assertEquals(FruitTransaction.Operation.SUPPLY, transactions.get(1).getOperation());
        assertEquals("apple", transactions.get(1).getFruit());
        assertEquals(100, transactions.get(1).getQuantity());

        assertEquals(FruitTransaction.Operation.PURCHASE, transactions.get(2).getOperation());
        assertEquals("banana", transactions.get(2).getFruit());
        assertEquals(13, transactions.get(2).getQuantity());

        assertEquals(FruitTransaction.Operation.RETURN, transactions.get(3).getOperation());
        assertEquals("apple", transactions.get(3).getFruit());
        assertEquals(10, transactions.get(3).getQuantity());
    }

    @Test
    void shouldHandleWhitespaceCorrectly() {
        List<String> rawData = List.of(
                "fruit,quantity",
                "  b , apple  , 10  "
        );
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(rawData);
        assertEquals(1, transactions.size());
        assertEquals(FruitTransaction.Operation.BALANCE, transactions.get(0).getOperation());
        assertEquals("apple", transactions.get(0).getFruit());
        assertEquals(10, transactions.get(0).getQuantity());
    }

    @ParameterizedTest
    @MethodSource("invalidData")
    void shouldThrowExceptionForInvalidData(String line, String expectedMessagePart) {
        List<String> rawData = List.of("fruit,quantity", line);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dataConverter.convertToTransaction(rawData));
        assertTrue(exception.getMessage().contains(expectedMessagePart));
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of("b,apple", "Invalid number of columns"),
                Arguments.of("b,apple,", "Invalid number of columns"),
                Arguments.of("b,,10", "Fruit name cannot be empty"),
                Arguments.of("b,apple,ten", "Invalid quantity format"),
                Arguments.of("b,apple,-5", "Quantity cannot be negative"),
                Arguments.of("x,apple,10", "Invalid code operation")
        );
    }
}
