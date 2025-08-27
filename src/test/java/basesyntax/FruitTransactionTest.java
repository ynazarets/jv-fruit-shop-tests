package basesyntax;

import basesyntax.common.FruitTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FruitTransaction.Operation enum")
class FruitTransactionTest {

    @Test
    @DisplayName("should return correct Operation for valid code")
    void fromCode_validCode_shouldReturnCorrectOperation() {
        Assertions.assertEquals(FruitTransaction.Operation.BALANCE,
                FruitTransaction.Operation.fromCode("b"));
        Assertions.assertEquals(FruitTransaction.Operation.SUPPLY,
                FruitTransaction.Operation.fromCode("s"));
        Assertions.assertEquals(FruitTransaction.Operation.PURCHASE,
                FruitTransaction.Operation.fromCode("p"));
        Assertions.assertEquals(FruitTransaction.Operation.RETURN,
                FruitTransaction.Operation.fromCode("r"));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for invalid code")
    void fromCode_invalidCode_shouldThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FruitTransaction.Operation.fromCode("x"));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FruitTransaction.Operation.fromCode(null));
    }
}
