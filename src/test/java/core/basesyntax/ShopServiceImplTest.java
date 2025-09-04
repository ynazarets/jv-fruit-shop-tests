package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;
import core.basesyntax.service.OperationStrategy;
import core.basesyntax.service.ShopService;
import core.basesyntax.serviceimpl.OperationStrategyImpl;
import core.basesyntax.serviceimpl.ShopServiceImpl;
import core.basesyntax.strategy.BalanceOperation;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.PurchaseOperation;
import core.basesyntax.strategy.ReturnOperation;
import core.basesyntax.strategy.SupplyOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShopServiceImpl")
class ShopServiceImplTest {

    private ShopService shopService;

    @BeforeEach
    void setUp() {
        Map<FruitTransaction.Operation, OperationHandler> handlerMap = new HashMap<>();
        handlerMap.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        handlerMap.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        handlerMap.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        handlerMap.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(handlerMap);
        shopService = new ShopServiceImpl(operationStrategy);
        Storage.clear();
    }

    @Test
    @DisplayName("should process transactions and update storage correctly")
    void process_validTransactions_shouldUpdateStorage() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "apple", 100),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, "apple", 50),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "apple", 25)
        );
        shopService.process(transactions);
        assertEquals(125, Storage.get("apple"));
    }

    @Test
    @DisplayName("should throw an exception for an invalid transaction in the list")
    void process_invalidTransaction_shouldThrowException() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "apple", 100),
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "apple", 200)
        );
        assertThrows(RuntimeException.class, () -> shopService.process(transactions));
    }
}
