package core.basesyntax.strategy;

import core.basesyntax.common.FruitTransaction;
import core.basesyntax.db.Storage;

public class SupplyOperation implements OperationHandler {
    @Override
    public void apply(FruitTransaction transaction) {
        validateTransaction(transaction);
        if (transaction.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0: "
                    + transaction.getQuantity());
        }
        Storage.add(transaction.getFruit(), transaction.getQuantity());
    }

    private void validateTransaction(FruitTransaction transaction) {
        if (transaction == null || transaction.getFruit() == null
                || transaction.getFruit().isBlank()) {
            throw new RuntimeException("Invalid transaction:"
                    + " Fruit name cannot be null or empty.");
        }
    }
}
