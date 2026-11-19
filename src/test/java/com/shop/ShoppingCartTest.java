package com.shop;

import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.entry;

class ShoppingCartTest {

    final ShoppingCart cart = new ShoppingCart(Set.of(
        Product.builder()
            .id("prod_1")
            .name("Смартфон")
            .price(500.0)
            .stock(5)
            .build(),

        Product.builder()
            .id("prod_2")
            .name("Чехол")
            .price(10.5)
            .stock(100)
            .build()
    ));

    @Test
    void givenNewCart_thenItemsAreEmptyAndDiscountIsZero() {
        assertThat(cart.getItems())
            .isEmpty();
        assertThat(cart.getDiscountPercent())
            .isZero();
    }

    @Test
    void givenEmptyCart_whenAddItem_thenItemIsAdded() {
        cart.addItem("prod_1", 2);

        assertThat(cart.getItems())
            .containsExactly(entry("prod_1", 2));
    }

    @Test
    void givenItemInCart_whenAddSameItem_thenQuantityIncreases() {
        cart.addItem("prod_1", 2);
        cart.addItem("prod_1", 3);

        assertThat(cart.getItems())
            .containsEntry("prod_1", 5);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -3, -100})
    void givenInvalidQuantity_whenAddItem_thenThrowsException(final int badQuantity) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> cart.addItem("prod_1", badQuantity));
    }

    @Test
    void givenUnknownProduct_whenAddItem_thenThrowsException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> cart.addItem("nope", 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 10, 100})
    void givenQuantityExceedingStock_whenAddItem_thenReturnsFalse(final int quantity) {
        assertThat(cart.addItem("prod_1", quantity))
            .isFalse();
        assertThat(cart.getItems())
            .isEmpty();
    }

    @Test
    void givenItemInCart_whenAddMoreThanStockLeft_thenReturnsFalseAndQuantityUnchanged() {
        assertThat(cart.addItem("prod_1", 3))
            .isTrue();
        assertThat(cart.addItem("prod_1", 3))
            .isFalse();
        assertThat(cart.getItems())
            .containsEntry("prod_1", 3);
    }

    @Test
    void givenItemInCart_whenRemovePart_thenQuantityDecreases() {
        cart.addItem("prod_1", 4);
        cart.removeItem("prod_1", 1);

        assertThat(cart.getItems())
            .containsEntry("prod_1", 3);
    }

    @Test
    void givenItemInCart_whenRemoveQuantityEqualOrMore_thenItemIsRemoved() {
        cart.addItem("prod_1", 2);
        cart.addItem("prod_2", 2);

        assertThat(cart.removeItem("prod_1", 2))
            .isTrue();
        assertThat(cart.removeItem("prod_2", 50))
            .isTrue();
        assertThat(cart.getItems())
            .isEmpty();
    }

    @Test
    void givenItemInCart_whenRemoveWithoutQuantity_thenReturnsTrueAndItemIsRemoved() {
        cart.addItem("prod_1", 4);

        assertThat(cart.removeItem("prod_1"))
            .isTrue();
        assertThat(cart.getItems())
            .isEmpty();
    }

    @Test
    void givenItemNotInCart_whenRemove_thenReturnsFalse() {
        assertThat(cart.removeItem("prod_1"))
            .isFalse();
        assertThat(cart.removeItem("prod_1", 1))
            .isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void givenItemInCart_whenRemoveInvalidQuantity_thenThrowsException(final int badQuantity) {
        cart.addItem("prod_1", 3);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> cart.removeItem("prod_1", badQuantity));
        assertThat(cart.getItems())
            .containsEntry("prod_1", 3);
    }

    @Test
    void givenEmptyCart_whenGetTotal_thenReturnsZero() {
        assertThat(cart.getTotal())
            .isCloseTo(0.0, within(0.001));
    }

    @Test
    void givenCartWithItems_whenGetTotal_thenReturnsSum() {
        cart.addItem("prod_1", 2);
        cart.addItem("prod_2", 3);

        assertThat(cart.getTotal())
            .isCloseTo(1031.5, within(0.001));
    }

    @Test
    void givenCart_whenGetItems_thenReturnsUnmodifiableMap() {
        cart.addItem("prod_1", 1);

        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> cart.getItems().put("prod_1", 99));
    }

    @ParameterizedTest
    @NullSource
    void givenNullProductId_whenAddItem_thenThrowsNullPointerException(@Nullable final String nullId) {
        assertThatNullPointerException()
            .isThrownBy(() -> cart.addItem(nullId, 1))
            .withMessageContaining("productId");
    }

    @ParameterizedTest
    @NullSource
    void givenNullPromoText_whenApplyPromo_thenThrowsNullPointerException(@Nullable final String nullText) {
        assertThatNullPointerException()
            .isThrownBy(() -> cart.applyPromoCode(nullText))
            .withMessageContaining("text");
    }

    @ParameterizedTest
    @NullSource
    void givenNullProductId_whenRemoveItem_thenThrowsNullPointerException(@Nullable final String nullId) {
        assertThatNullPointerException()
            .isThrownBy(() -> cart.removeItem(nullId))
            .withMessageContaining("productId");
    }

    @ParameterizedTest
    @NullSource
    void givenNullProductId_whenRemoveItemWithQuantity_thenThrowsNullPointerException(@Nullable final String nullId) {
        assertThatNullPointerException()
            .isThrownBy(() -> cart.removeItem(nullId, 1))
            .withMessageContaining("productId");
    }

    @ParameterizedTest
    @NullSource
    void givenNullProducts_whenConstructorShoppingCart_thenThrowsNullPointerException(@Nullable final Set<Product> nullProducts) {
        assertThatNullPointerException()
            .isThrownBy(() -> new ShoppingCart(nullProducts))
            .withMessageContaining("products");
    }

    @Test
    void givenDuplicateProductIds_whenCreateCart_thenThrowsIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new ShoppingCart(Set.of(
                Product.builder()
                    .id("prod_1")
                    .name("Смартфон")
                    .price(500.0)
                    .stock(5)
                    .build(),

                Product.builder()
                    .id("prod_1")
                    .name("Планшет")
                    .price(300.0)
                    .stock(2)
                    .build()
            )))
            .withMessage("Дублирующийся id товара: prod_1");
    }
}