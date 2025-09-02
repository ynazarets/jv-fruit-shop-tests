package core.basesyntax.strategy;

import core.basesyntax.common.FruitTransaction;

public interface OperationHandler {
    void apply(FruitTransaction transaction);
}
