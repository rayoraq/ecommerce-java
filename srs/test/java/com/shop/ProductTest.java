package com.shop;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @Test
    void givenValidData_whenCreateProduct_thenAttributesAreSet() {
        Product p = new Product("prod_102", "Смартфон", 999.99, 3);
        assertEquals("prod_102", p.getId());
        assertEquals("Смартфон", p.getName());
        assertEquals(999.99, p.getPrice(), 0.001);
        assertEquals(3, p.getStock());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidId_whenCreateProduct_thenThrowsIllegalArgumentException(String badId) {
        assertThrows(IllegalArgumentException.class, () -> new Product(badId, "N", 1, 1));
    }

    @ParameterizedTest
    @NullAndEmptySource
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