package core.basesyntax.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.common.FruitTransaction;
import core.basesyntax.service.handler.BalanceOperation;
import core.basesyntax.service.handler.OperationHandler;
import core.basesyntax.service.handler.PurchaseOperation;
import core.basesyntax.service.handler.ReturnOperation;
import core.basesyntax.service.handler.SupplyOperation;
import core.basesyntax.service.shop.OperationStrategyImpl;
import core.basesyntax.service.shop.ShopServiceImpl;
import core.basesyntax.storage.Storage;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private ShopServiceImpl shopService;

    @BeforeEach
    void setUp() {
        Map<FruitTransaction.Operation, OperationHandler> handlerMap = new TreeMap<>();
        handlerMap.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        handlerMap.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        handlerMap.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        handlerMap.put(FruitTransaction.Operation.RETURN, new ReturnOperation());

        OperationStrategyImpl operationStrategy = new OperationStrategyImpl(handlerMap);
        shopService = new ShopServiceImpl(operationStrategy);
    }

    @AfterEach
    void tearDown() {
        Map<String, Integer> fruits = Storage.getFruits();
        fruits.clear();
    }

    @Test
    void shouldProcessTransactionsInOrder() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "apple", 50),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "apple", 20)
        );

        shopService.process(transactions);
        assertEquals(30, Storage.get("apple"));
    }

    @Test
    void shouldThrowExceptionIfAnyTransactionFails() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "apple", 10),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "apple", 20)
        );

        assertThrows(RuntimeException.class,
                () -> shopService.process(transactions));
    }
}
