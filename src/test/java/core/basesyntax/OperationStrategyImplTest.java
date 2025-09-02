package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.service.OperationStrategy;
import core.basesyntax.serviceimpl.OperationStrategyImpl;
import core.basesyntax.strategy.BalanceOperation;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.SupplyOperation;
import java.util.HashMap;
import java.util.Map;
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
        assertInstanceOf(BalanceOperation.class, handler);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for an unsupported operation")
    void getOperationHandler_unsupportedOperation_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                operationStrategy.getOperationHandler(FruitTransaction.Operation.PURCHASE));
    }
}
