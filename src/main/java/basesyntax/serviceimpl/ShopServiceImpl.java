package basesyntax.serviceimpl;

import basesyntax.common.FruitTransaction;
import basesyntax.service.OperationStrategy;
import basesyntax.service.ShopService;
import basesyntax.strategy.OperationHandler;
import java.util.List;

public class ShopServiceImpl implements ShopService {
    private final OperationStrategy operationStrategy;

    public ShopServiceImpl(OperationStrategy operationStrategy) {
        this.operationStrategy = operationStrategy;
    }

    @Override
    public void process(List<FruitTransaction> transactions) {
        for (FruitTransaction transaction : transactions) {
            OperationHandler handler = operationStrategy.getOperationHandler(
                    transaction.getOperation());
            handler.apply(transaction);
        }
    }
}
