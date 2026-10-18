package com.shop;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ShoppingCartTest {
    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart(List.of(
                new Product("prod_1", "Смартфон", 500.0, 5),
                new Product("prod_2", "Чехол", 10.5, 100)));
    }

    @Test
    void givenNewCart_thenItemsAreEmpty() {
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void givenEmptyCart_whenAddItem_thenItemIsAdded() {
        cart.addItem("prod_1", 2);
        assertEquals(Map.of("prod_1", 2), cart.getItems());
    }

    @Test
    void givenItemInCart_whenAddSameItem_thenQuantityIncreases() {
        cart.addItem("prod_1", 2);
        cart.addItem("prod_1", 3);
        assertEquals(5, cart.getItems().get("prod_1"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -3, -100})
    void givenInvalidQuantity_whenAddItem_thenThrowsException(int badQuantity) {
        assertThrows(CartException.class, () -> cart.addItem("prod_1", badQuantity));
    }

    @Test
    void givenUnknownProduct_whenAddItem_thenThrowsException() {
        assertThrows(CartException.class, () -> cart.addItem("nope", 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 10, 100})
    void givenQuantityExceedingStock_whenAddItem_thenThrowsException(int quantity) {
        assertThrows(CartException.class, () -> cart.addItem("prod_1", quantity));
    }

    @Test
    void givenItemInCart_whenAddMoreThanStockLeft_thenThrowsException() {
        cart.addItem("prod_1", 3);
        assertThrows(CartException.class, () -> cart.addItem("prod_1", 3));
        assertEquals(3, cart.getItems().get("prod_1"));
    }

    @Test
    void givenItemInCart_whenRemovePart_thenQuantityDecreases() {
        cart.addItem("prod_1", 4);
        cart.removeItem("prod_1", 1);
        assertEquals(3, cart.getItems().get("prod_1"));
    }

    @Test
    void givenItemInCart_whenRemoveQuantityEqualOrMore_thenItemIsRemoved() {
        cart.addItem("prod_1", 2);
        cart.addItem("prod_2", 2);
        cart.removeItem("prod_1", 2);
        cart.removeItem("prod_2", 50);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void givenItemInCart_whenRemoveWithoutQuantity_thenItemIsRemoved() {
        cart.addItem("prod_1", 4);
        cart.removeItem("prod_1");
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void givenItemNotInCart_whenRemove_thenThrowsException() {
        assertThrows(CartException.class, () -> cart.removeItem("prod_1"));
        assertThrows(CartException.class, () -> cart.removeItem("prod_1", 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void givenItemInCart_whenRemoveInvalidQuantity_thenThrowsException(int badQuantity) {
        cart.addItem("prod_1", 3);
        assertThrows(CartException.class, () -> cart.removeItem("prod_1", badQuantity));
        assertEquals(3, cart.getItems().get("prod_1"));
    }

    @Test
    void givenEmptyCart_whenGetTotal_thenReturnsZero() {
        assertEquals(0.0, cart.getTotal(), 0.001);
    }

    @Test
    void givenCartWithItems_whenGetTotal_thenReturnsSum() {
        cart.addItem("prod_1", 2);
        cart.addItem("prod_2", 3);
        assertEquals(1031.5, cart.getTotal(), 0.001);
    }

    @Test
    void givenCart_whenGetItems_thenReturnsUnmodifiableMap() {
        cart.addItem("prod_1", 1);
        assertThrows(UnsupportedOperationException.class, () -> cart.getItems().put("prod_1", 99));
    }
}