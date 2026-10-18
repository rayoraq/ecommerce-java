package com.shop;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ShoppingCartPromoTest {
    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart(List.of(new Product("prod_1", "Смартфон", 500.0, 5)));
        cart.addItem("prod_1", 2);
    }

    @Test
    void givenNoPromoByDefault_whenCheckDiscount_thenReturnsZeroAndOriginalTotal() {
        assertEquals(0, cart.getDiscountPercent());
        assertEquals(1000.0, cart.getTotal(), 0.001);
    }

    @ParameterizedTest(name = "Промокод {0} дает скидку {1}% и сумму {2}")
    @CsvSource(
            ignoreLeadingAndTrailingWhitespace = false,
            value = {
                    "SALE10, 10, 900.0",
                    "'  sale20 ', 20, 800.0",
                    "HALF, 50, 500.0",
                    "HOT, 90, 100.0"
            }
    )
    void givenValidPromo_whenApplyPromo_thenAppliesCorrectDiscount(String code, int expectedDiscount, double expectedTotal) {
        cart.applyPromoCode(code);
        assertEquals(expectedDiscount, cart.getDiscountPercent());
        assertEquals(expectedTotal, cart.getTotal(), 0.001);
    }

    @Test
    void givenPromoApplied_whenNewPromoApplied_thenOldPromoIsReplaced() {
        cart.applyPromoCode("SALE10");
        cart.applyPromoCode("SALE20");
        assertEquals(20, cart.getDiscountPercent());
    }

    @ParameterizedTest(name = "Невалидный промокод: [{0}]")
    @NullAndEmptySource
    @ValueSource(strings = {"WRONG", "SALE99", "HOT100"})
    void givenInvalidPromo_whenApplyPromo_thenThrowsExceptionAndDiscountRemainsZero(String badCode) {
        assertThrows(CartException.class, () -> cart.applyPromoCode(badCode));
        assertEquals(0, cart.getDiscountPercent());
    }
}