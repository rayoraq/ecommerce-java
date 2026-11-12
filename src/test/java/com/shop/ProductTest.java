package com.shop;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ProductTest {

    @Test
    void givenValidData_whenCreateProduct_thenAttributesAreSet() {
        val product = new Product("prod_102", "Смартфон", 999.99, 3);
        assertThat(product)
            .extracting(Product::id, Product::name, Product::price, Product::stock)
            .containsExactly("prod_102", "Смартфон", 999.99, 3);
    }

    @Test
    void givenNullId_whenCreateProduct_thenThrowsNullPointerException() {
        assertThatNullPointerException()
            .isThrownBy(() -> new Product(null, "N", 1, 1))
            .withMessage("id is marked non-null but is null");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidId_whenCreateProduct_thenThrowsIllegalArgumentException(final String badId) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Product(badId, "N", 1, 1))
            .withMessage("id должен быть непустым");
    }

    @Test
    void givenNullName_whenCreateProduct_thenThrowsNullPointerException() {
        assertThatNullPointerException()
            .isThrownBy(() -> new Product("p", null, 1, 1))
            .withMessage("name is marked non-null but is null");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidName_whenCreateProduct_thenThrowsIllegalArgumentException(final String badName) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Product("p", badName, 1, 1))
            .withMessage("name должен быть непустым");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.01, -999.99})
    void givenNegativePrice_whenCreateProduct_thenThrowsIllegalArgumentException(final double badPrice) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Product("p", "N", badPrice, 1))
            .withMessage("price не может быть отрицательной");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    void givenNegativeStock_whenCreateProduct_thenThrowsIllegalArgumentException(final int badStock) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Product("p", "N", 1, badStock))
            .withMessage("stock не может быть отрицательным");
    }
}