package basesyntax;

import basesyntax.common.FruitTransaction;
import basesyntax.service.OperationStrategy;
import basesyntax.serviceimpl.OperationStrategyImpl;
import basesyntax.strategy.BalanceOperation;
import basesyntax.strategy.OperationHandler;
import basesyntax.strategy.SupplyOperation;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OperationStrategyImpl")
class OperationStrategyImplTest {

    private OperationStrategy operationStrategy;

    @BeforeEach
    void setUp() {
        Map<FruitTransaction.Operation, OperationHandler> handlerMap = new HashMap<>();
        handlerMap.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        handlerMap.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        operationStrategy = new OperationStrategyImpl(handlerMap);
    }

    @Test
    @DisplayName("should return correct handler for a given operation")
    void getOperationHandler_validOperation_shouldReturnCorrectHandler() {
        OperationHandler handler = operationStrategy
                .getOperationHandler(FruitTransaction.Operation.BALANCE);
        Assertions.assertInstanceOf(BalanceOperation.class, handler);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for an unsupported operation")
    void getOperationHandler_unsupportedOperation_shouldThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                operationStrategy.getOperationHandler(FruitTransaction.Operation.PURCHASE));
    }
}