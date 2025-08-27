package core.basesyntax.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.common.FruitTransaction;
import core.basesyntax.service.handler.BalanceOperation;
import core.basesyntax.service.handler.OperationHandler;
import core.basesyntax.service.handler.PurchaseOperation;
import core.basesyntax.service.handler.ReturnOperation;
import core.basesyntax.service.handler.SupplyOperation;
import core.basesyntax.storage.Storage;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OperationHandlersTest {
    @AfterEach
    void tearDown() {
        Map<String, Integer> fruits = Storage.getFruits();
        fruits.clear();
    }

    @Test
    void balanceOperation_shouldPutFruitAndQuantityInStorage() {
        OperationHandler handler = new BalanceOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, "banana", 100);
        handler.apply(transaction);
        assertEquals(100, Storage.get("banana"));
    }

    @Test
    void balanceOperation_shouldThrowExceptionForInvalidTransaction() {
        OperationHandler handler = new BalanceOperation();
        assertThrows(RuntimeException.class,
                () -> handler.apply(null));
        assertThrows(RuntimeException.class,
                () -> handler.apply(new FruitTransaction(
                        FruitTransaction.Operation.BALANCE, null, 10)));
        assertThrows(RuntimeException.class,
                () -> handler.apply(new FruitTransaction(
                        FruitTransaction.Operation.BALANCE, " ", 10)));
    }

    @Test
    void balanceOperation_shouldThrowExceptionForNegativeQuantity() {
        OperationHandler handler = new BalanceOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, "banana", -10);
        assertThrows(RuntimeException.class,
                () -> handler.apply(transaction));
    }

    @Test
    void supplyOperation_shouldAddQuantityToExistingFruit() {
        Storage.put("apple", 50);
        OperationHandler handler = new SupplyOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, "apple", 20);
        handler.apply(transaction);
        assertEquals(70, Storage.get("apple"));
    }

    @Test
    void supplyOperation_shouldThrowExceptionForZeroOrNegativeQuantity() {
        OperationHandler handler = new SupplyOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, "apple", 0);
        assertThrows(RuntimeException.class,
                () -> handler.apply(transaction));
    }

    @Test
    void purchaseOperation_shouldSubtractQuantityFromExistingFruit() {
        Storage.put("banana", 100);
        OperationHandler handler = new PurchaseOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "banana", 30);
        handler.apply(transaction);
        assertEquals(70, Storage.get("banana"));
    }

    @Test
    void purchaseOperation_shouldThrowExceptionForInsufficientQuantity() {
        Storage.put("banana", 10);
        OperationHandler handler = new PurchaseOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "banana", 20);
        assertThrows(RuntimeException.class,
                () -> handler.apply(transaction));
    }

    @Test
    void purchaseOperation_shouldThrowExceptionForZeroOrNegativeQuantity() {
        Storage.put("banana", 10);
        OperationHandler handler = new PurchaseOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "banana", 0);
        assertThrows(RuntimeException.class,
                () -> handler.apply(transaction));
    }

    @Test
    void returnOperation_shouldAddQuantityToExistingFruit() {
        Storage.put("apple", 50);
        OperationHandler handler = new ReturnOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.RETURN, "apple", 10);
        handler.apply(transaction);
        assertEquals(60, Storage.get("apple"));
    }

    @Test
    void returnOperation_shouldThrowExceptionForZeroOrNegativeQuantity() {
        OperationHandler handler = new ReturnOperation();
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.RETURN, "apple", 0);
        assertThrows(RuntimeException.class,
                () -> handler.apply(transaction));
    }
}
