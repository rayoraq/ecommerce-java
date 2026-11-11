package com.shop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void givenValidData_whenCreateProduct_thenAttributesAreSet() {
        Product p = new Product("prod_102", "Смартфон", 999.99, 3);
        assertEquals("prod_102", p.id());
        assertEquals("Смартфон", p.name());
        assertEquals(999.99, p.price(), 0.001);
        assertEquals(3, p.stock());
    }

    @Test
    void givenNullId_whenCreateProduct_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Product(null, "N", 1, 1));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidId_whenCreateProduct_thenThrowsIllegalArgumentException(String badId) {
        assertThrows(IllegalArgumentException.class, () -> new Product(badId, "N", 1, 1));
    }

    @Test
    void givenNullName_whenCreateProduct_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Product("p", null, 1, 1));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidName_whenCreateProduct_thenThrowsIllegalArgumentException(String badName) {
        assertThrows(IllegalArgumentException.class, () -> new Product("p", badName, 1, 1));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.01, -999.99})
    void givenNegativePrice_whenCreateProduct_thenThrowsIllegalArgumentException(double badPrice) {
        assertThrows(IllegalArgumentException.class, () -> new Product("p", "N", badPrice, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    void givenNegativeStock_whenCreateProduct_thenThrowsIllegalArgumentException(int badStock) {
        assertThrows(IllegalArgumentException.class, () -> new Product("p", "N", 1, badStock));
    }
}