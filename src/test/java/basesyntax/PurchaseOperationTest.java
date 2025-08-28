package basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import basesyntax.common.FruitTransaction;
import basesyntax.db.Storage;
import basesyntax.strategy.OperationHandler;
import basesyntax.strategy.PurchaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PurchaseOperation")
class PurchaseOperationTest {

    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new PurchaseOperation();
        Storage.clear();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    @DisplayName("should correctly subtract quantity from existing fruit")
    void apply_shouldSubtractQuantity() {
        Storage.put("apple", 50);
        handler.apply(new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "apple", 20));
        assertEquals(30, Storage.get("apple"));
    }

    @Test
    @DisplayName("should throw RuntimeException for insufficient stock")
    void apply_insufficientStock_shouldThrowException() {
        Storage.put("apple", 5);
        assertThrows(RuntimeException.class, () ->
                handler.apply(new FruitTransaction(
                        FruitTransaction.Operation.PURCHASE, "apple", 10)));
    }

    @Test
    @DisplayName("should throw RuntimeException for zero quantity")
    void apply_zeroQuantity_shouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                handler.apply(new FruitTransaction(
                        FruitTransaction.Operation.PURCHASE, "apple", 0)));
    }
}
