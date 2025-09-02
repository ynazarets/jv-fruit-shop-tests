package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.SupplyOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SupplyOperation")
class SupplyOperationTest {

    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SupplyOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    @DisplayName("should correctly add quantity to existing fruit")
    void apply_existingFruit_shouldAddQuantity() {
        Storage.put("apple", 50);
        handler.apply(new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, "apple", 20));
        assertEquals(70, Storage.get("apple"));
    }

    @Test
    @DisplayName("should correctly add quantity to a new fruit")
    void apply_newFruit_shouldSetQuantity() {
        handler.apply(new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, "orange", 20));
        assertEquals(20, Storage.get("orange"));
    }

    @Test
    @DisplayName("should throw RuntimeException for zero quantity")
    void apply_zeroQuantity_shouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                handler.apply(new FruitTransaction(
                        FruitTransaction.Operation.SUPPLY, "apple", 0)));
    }
}
