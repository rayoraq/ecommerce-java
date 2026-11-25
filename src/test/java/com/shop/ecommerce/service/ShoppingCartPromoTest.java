package com.shop.ecommerce.service;

import com.shop.ecommerce.beans.Product;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

class ShoppingCartPromoTest {

    final ShoppingCart cart = new ShoppingCart(Set.of(
        Product.builder()
            .id("prod_1")
            .name("Смартфон")
            .price(500.0)
            .stock(5)
            .build()));

    @BeforeEach
    void setUp() {
        cart.addItem("prod_1", 2);
    }

    @Test
    void givenNoPromoByDefault_whenCheckDiscount_thenReturnsZeroAndOriginalTotal() {
        assertThat(cart.getDiscountPercent())
            .isZero();
        assertThat(cart.getTotal())
            .isCloseTo(1000.0, within(0.001));
    }

    @ParameterizedTest(name = "Промокод {0} дает скидку {1}% и сумму {2}")
    @CsvSource(
        value = {
            "SALE10, 10, 900.0",
            "'  sale20 ', 20, 800.0",
            "HALF, 50, 500.0",
            "HOT, 90, 100.0"
        }
    )
    void givenValidPromo_whenApplyPromo_thenAppliesCorrectDiscount(
        @NonNull final String code,
        int expectedDiscount,
        double expectedTotal
    ) {

        assertThat(cart.applyPromoCode(code))
            .isTrue();
        assertThat(cart.getDiscountPercent())
            .isEqualTo(expectedDiscount);
        assertThat(cart.getTotal())
            .isCloseTo(expectedTotal, within(0.001));
    }

    @Test
    void givenPromoApplied_whenNewPromoApplied_thenOldPromoIsReplaced() {
        assertThat(cart.applyPromoCode("SALE10"))
            .isTrue();
        assertThat(cart.applyPromoCode("SALE20"))
            .isTrue();
        assertThat(cart.getDiscountPercent())
            .isEqualTo(20);
        assertThat(cart.getTotal())
            .isCloseTo(800.0, within(0.001));
    }

    @ParameterizedTest(name = "Невалидный промокод: [{0}]")
    @EmptySource
    @ValueSource(strings = {" ", "WRONG", "SALE99", "HOT100"})
    void givenInvalidPromo_whenApplyPromo_thenReturnsFalseAndDiscountRemainsZero(final String badCode) {
        assertThat(cart.applyPromoCode(badCode))
            .isFalse();
        assertThat(cart.getDiscountPercent())
            .isZero();
        assertThat(cart.getTotal())
            .isCloseTo(1000.0, within(0.001));
    }

    @Test
    void givenPromoApplied_whenInvalidPromoApplied_thenReturnsFalseAndOldPromoRemains() {
        assertThat(cart.applyPromoCode("SALE20"))
            .isTrue();
        assertThat(cart.applyPromoCode("WRONG"))
            .isFalse();
        assertThat(cart.getDiscountPercent())
            .isEqualTo(20);
        assertThat(cart.getTotal())
            .isCloseTo(800.0, within(0.001));
    }
}