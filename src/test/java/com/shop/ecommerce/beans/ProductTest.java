package com.shop.ecommerce.beans;

import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.shop.ecommerce.utils.BeanValidator.validate;
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

        val productWithConstructor = validate(new Product("prod_102", "Смартфон", 999.99, 3));

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
        assertThatCode(() -> validate(product))
            .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullSource
    void givenNullId_whenCreateProduct_thenThrowsNullPointerException(@Nullable final String nullId) {
        assertThatNullPointerException()
            .isThrownBy(() -> validProduct().id(nullId)
                .build())
            .withMessage("id is marked non-null but is null");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidId_whenCreateProduct_thenThrowsIllegalArgumentException(@NonNull final String badId) {
        val product = validProduct()
            .id(badId)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validate(product))
            .withMessageContaining("id не может быть пустым");
    }

    @ParameterizedTest
    @NullSource
    void givenNullName_whenCreateProduct_thenThrowsNullPointerException(@Nullable final String nullName) {
        assertThatNullPointerException()
            .isThrownBy(() -> validProduct().name(nullName)
                .build())
            .withMessage("name is marked non-null but is null");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void givenInvalidName_whenCreateProduct_thenThrowsIllegalArgumentException(@NonNull final String badName) {
        val product = validProduct()
            .name(badName)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validate(product))
            .withMessageContaining("name не может быть пустым");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -0.01, -999.99})
    void givenNegativePrice_whenCreateProduct_thenThrowsIllegalArgumentException(final double badPrice) {
        val product = validProduct()
            .price(badPrice)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validate(product))
            .withMessageContaining("price не может быть отрицательной");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    void givenNegativeStock_whenCreateProduct_thenThrowsIllegalArgumentException(final int badStock) {
        var product = validProduct()
            .stock(badStock)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validate(product))
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
        assertThatCode(() -> validate(product))
            .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void givenNonFinitePrice_whenValidateProduct_thenHasViolation(final double nonFinitePrice) {
        val product = validProduct()
            .price(nonFinitePrice)
            .build();

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> validate(product))
            .withMessageContaining("price должна быть конечной");
    }
}