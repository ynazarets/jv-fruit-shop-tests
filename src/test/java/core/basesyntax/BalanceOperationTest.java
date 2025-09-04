package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;
import core.basesyntax.strategy.BalanceOperation;
import core.basesyntax.strategy.OperationHandler;
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
