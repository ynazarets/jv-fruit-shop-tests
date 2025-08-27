package core.basesyntax.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.service.common.FruitTransaction;
import core.basesyntax.service.handler.BalanceOperation;
import core.basesyntax.service.handler.OperationHandler;
import core.basesyntax.service.handler.SupplyOperation;
import core.basesyntax.service.shop.OperationStrategyImpl;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationStrategyImplTest {
    private OperationStrategyImpl operationStrategy;

    @BeforeEach
    void setUp() {
        Map<FruitTransaction.Operation, OperationHandler> handlerMap = new TreeMap<>();
        handlerMap.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        handlerMap.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        operationStrategy = new OperationStrategyImpl(handlerMap);
    }

    @Test
    void shouldReturnCorrectHandlerForOperation() {
        OperationHandler balanceHandler = operationStrategy.getOperationHandler(
                FruitTransaction.Operation.BALANCE);
        assertNotNull(balanceHandler);
        assertEquals(BalanceOperation.class, balanceHandler.getClass());

        OperationHandler supplyHandler = operationStrategy.getOperationHandler(
                FruitTransaction.Operation.SUPPLY);
        assertNotNull(supplyHandler);
        assertEquals(SupplyOperation.class, supplyHandler.getClass());
    }

    @Test
    void shouldThrowExceptionForUnknownOperation() {
        assertThrows(IllegalArgumentException.class,
                () -> operationStrategy.getOperationHandler(
                        FruitTransaction.Operation.PURCHASE));
    }
}
