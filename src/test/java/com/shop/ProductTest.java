package com.shop;

import jakarta.validation.ConstraintViolationException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.shop.BeanValidator.validated;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ProductTest {

    private static Product.ProductBuilder validProduct() {
        return Product.builder()
            .id("p")
            .name("N")
            .price(1)
            .stock(1);
    }

    @Test
    void givenValidData_whenCreateByConstructor_thenEqualsProductBuiltByBuilder() {
        val product = Product.builder()
            .id("prod_102")
            .name("Смартфон")
            .price(999.99)
            .stock(3)
            .build();

        val productWithConstructor = validated(new Product("prod_102", "Смартфон", 999.99, 3));

        assertThat(productWithConstructor)
            .usingRecursiveComparison()
            .isEqualTo(product);
    }

    @Test
    void givenValidData_whenCreateProduct_thenAttributesAreSet() {
        val product = Product.builder()
            .id("prod_102")
            .name("Смартфон")
            .price(999.99)
            .stock(3)
            .build();

        assertThat(product)
            .extracting(Product::id, Product::name, Product::price, Product::stock)
            .containsExactly("prod_102", "Смартфон", 999.99, 3);

        assertThatCode(() -> validated(product))
            .doesNotThrowAnyException();
    }

    @Test
    void givenNullId_whenCreateProduct_thenThrowsNullPointerException() {
        assertThatNullPointerException()
            .isThrownBy(() -> validProduct().id(null).build())
            .withMessage("id is marked non-null but is null");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidId_whenCreateProduct_thenThrowsIllegalArgumentException(final String badId) {
        val product = validProduct()
            .id(badId)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validated(product))
            .withMessageContaining("id не может быть пустым");
    }

    @Test
    void givenNullName_whenCreateProduct_thenThrowsNullPointerException() {
        assertThatNullPointerException()
            .isThrownBy(() -> validProduct().name(null).build())
            .withMessage("name is marked non-null but is null");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidName_whenCreateProduct_thenThrowsIllegalArgumentException(final String badName) {
        val product = validProduct()
            .name(badName)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validated(product))
            .withMessageContaining("name не может быть пустым");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.01, -999.99})
    void givenNegativePrice_whenCreateProduct_thenThrowsIllegalArgumentException(final double badPrice) {
        val product = validProduct()
            .price(badPrice)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validated(product))
            .withMessageContaining("price не может быть отрицательной");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    void givenNegativeStock_whenCreateProduct_thenThrowsIllegalArgumentException(final int badStock) {
        var product = validProduct()
            .stock(badStock)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validated(product))
            .withMessageContaining("stock не может быть отрицательной");
    }

    @Test
    void givenZeroPriceAndStock_whenCreateProduct_thenProductIsCreated() {
        val product = validProduct()
            .price(0)
            .stock(0)
            .build();

        assertThat(product)
            .extracting(Product::price, Product::stock)
            .containsExactly(0.0, 0);

        assertThatCode(() -> validated(product))
            .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void givenNonFinitePrice_whenValidateProduct_thenHasViolation(final double nonFinitePrice) {
        val product = validProduct()
            .price(nonFinitePrice)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validated(product))
            .withMessageContaining("price должна быть конечной");
    }
}