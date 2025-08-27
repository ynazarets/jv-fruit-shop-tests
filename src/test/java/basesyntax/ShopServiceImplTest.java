package basesyntax;

import basesyntax.common.FruitTransaction;
import basesyntax.db.Storage;
import basesyntax.service.OperationStrategy;
import basesyntax.service.ShopService;
import basesyntax.serviceimpl.OperationStrategyImpl;
import basesyntax.serviceimpl.ShopServiceImpl;
import basesyntax.strategy.BalanceOperation;
import basesyntax.strategy.OperationHandler;
import basesyntax.strategy.PurchaseOperation;
import basesyntax.strategy.ReturnOperation;
import basesyntax.strategy.SupplyOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ShopServiceImpl and OperationHandlers")
class ShopServiceImplTest {

    private ShopService shopService;

    @BeforeEach
    void setUp() {
        Storage.getFruits().clear();
        Map<FruitTransaction.Operation, OperationHandler> handlerMap = new HashMap<>();
        handlerMap.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        handlerMap.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        handlerMap.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        handlerMap.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(handlerMap);
        shopService = new ShopServiceImpl(operationStrategy);
    }

    @Nested
    @DisplayName("Operation Handlers")
    class OperationHandlersTest {
        @Test
        @DisplayName("BalanceOperation should correctly set quantity")
        void balanceOperation_apply_shouldSetQuantity() {
            shopService.process(List.of(new FruitTransaction(
                    FruitTransaction.Operation.BALANCE, "apple", 100)));
            Assertions.assertEquals(100, Storage.get("apple"));
        }

        @Test
        @DisplayName("SupplyOperation should correctly add quantity")
        void supplyOperation_apply_shouldAddQuantity() {
            Storage.put("apple", 50);
            shopService.process(List.of(new FruitTransaction(
                    FruitTransaction.Operation.SUPPLY, "apple", 20)));
            Assertions.assertEquals(70, Storage.get("apple"));
        }

        @Test
        @DisplayName("PurchaseOperation should correctly subtract quantity")
        void purchaseOperation_apply_shouldSubtractQuantity() {
            Storage.put("apple", 50);
            shopService.process(List.of(new FruitTransaction(
                    FruitTransaction.Operation.PURCHASE, "apple", 20)));
            Assertions.assertEquals(30, Storage.get("apple"));
        }

        @Test
        @DisplayName("PurchaseOperation should throw RuntimeException for insufficient stock")
        void purchaseOperation_insufficientStock_shouldThrowException() {
            Storage.put("apple", 5);
            Assertions.assertThrows(RuntimeException.class, () ->
                    shopService.process(List.of(new FruitTransaction(
                            FruitTransaction.Operation.PURCHASE, "apple", 10))));
        }

        @Test
        @DisplayName("ReturnOperation should correctly add quantity")
        void returnOperation_apply_shouldAddQuantity() {
            Storage.put("apple", 50);
            shopService.process(List.of(new FruitTransaction(
                    FruitTransaction.Operation.RETURN, "apple", 20)));
            Assertions.assertEquals(70, Storage.get("apple"));
        }

        @Test
        @DisplayName("Operations should throw RuntimeException for invalid quantities")
        void operations_invalidQuantity_shouldThrowException() {
            Assertions.assertThrows(RuntimeException.class, () ->
                    new BalanceOperation().apply(new FruitTransaction(
                            FruitTransaction.Operation.BALANCE, "apple", -1)));
            Assertions.assertThrows(RuntimeException.class, () ->
                    new SupplyOperation().apply(new FruitTransaction(
                            FruitTransaction.Operation.SUPPLY, "apple", 0)));
            Assertions.assertThrows(RuntimeException.class, () ->
                    new PurchaseOperation().apply(new FruitTransaction(
                            FruitTransaction.Operation.PURCHASE, "apple", 0)));
            Assertions.assertThrows(RuntimeException.class, () ->
                    new ReturnOperation().apply(new FruitTransaction(
                            FruitTransaction.Operation.RETURN, "apple", 0)));
        }

        @Test
        @DisplayName("Operations should throw RuntimeException for null or empty fruit name")
        void operations_nullOrEmptyFruitName_shouldThrowException() {
            OperationHandler handler = new SupplyOperation();
            Assertions.assertThrows(RuntimeException.class, () ->
                    handler.apply(new FruitTransaction(
                            FruitTransaction.Operation.SUPPLY, null, 10)));
            Assertions.assertThrows(RuntimeException.class, () ->
                    handler.apply(new FruitTransaction(
                            FruitTransaction.Operation.SUPPLY, "", 10)));
            Assertions.assertThrows(RuntimeException.class, () ->
                    handler.apply(new FruitTransaction(
                            FruitTransaction.Operation.SUPPLY, "   ", 10)));
        }
    }
}
