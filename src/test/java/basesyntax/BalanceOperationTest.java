package basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import basesyntax.common.FruitTransaction;
import basesyntax.db.Storage;
import basesyntax.strategy.BalanceOperation;
import basesyntax.strategy.OperationHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BalanceOperation")
class BalanceOperationTest {

    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new BalanceOperation();
        Storage.clear();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    @DisplayName("should correctly set quantity for an existing fruit")
    void apply_existingFruit_shouldSetQuantity() {
        Storage.put("banana", 50);
        handler.apply(new FruitTransaction(
                FruitTransaction.Operation.BALANCE, "banana", 100));
        assertEquals(100, Storage.get("banana"));
    }

    @Test
    @DisplayName("should correctly set quantity for a new fruit")
    void apply_newFruit_shouldSetQuantity() {
        handler.apply(new FruitTransaction(
                FruitTransaction.Operation.BALANCE, "orange", 200));
        assertEquals(200, Storage.get("orange"));
    }

    @Test
    @DisplayName("should throw RuntimeException for negative quantity")
    void apply_negativeQuantity_shouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                handler.apply(new FruitTransaction(
                        FruitTransaction.Operation.BALANCE, "apple", -1)));
    }
}
