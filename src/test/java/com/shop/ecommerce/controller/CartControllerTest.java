package com.shop.ecommerce.controller;

import com.shop.ecommerce.service.CartManagerService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static com.shop.ecommerce.controller.ApiPaths.CART_BASE;
import static com.shop.ecommerce.controller.ApiPaths.CART_ITEMS;
import static com.shop.ecommerce.controller.ApiPaths.CART_ITEM_BY_ID;
import static com.shop.ecommerce.controller.ApiPaths.CART_PROMO;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CartController.class)
@Import({CartManagerService.class, GlobalExceptionHandler.class})
class CartControllerTest {
    private static final String PRODUCT_SMARTPHONE = "prod_1";
    @Autowired private MockMvcTester mockMvc;

    @Test
    void givenNewUser_whenGetCart_thenReturnsEmptyCart() {
        val result = mockMvc.get()
            .uri(CART_BASE, "user_empty")
            .exchange();

        assertThat(result).hasStatusOk()
            .bodyJson()
            .extractingPath("$.total")
            .isEqualTo(0.0);
    }

    @ParameterizedTest
    @CsvSource({
        "prod_1, 2, 1000.0",
        "prod_2, 4, 42.0"
    })
    void givenValidProduct_whenAddItem_thenReturnsUpdatedCart(
        final String productId,
        final int quantity,
        final double expectedTotal
    ) {
        val userId = "user_add_" + productId;
        val body = """
            {"productId": "%s", "quantity": %d}
            """.formatted(productId, quantity);
        val result = mockMvc.post()
            .uri(CART_ITEMS, userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(result).hasStatusOk()
            .bodyJson()
            .extractingPath("$.total")
            .isEqualTo(expectedTotal);
    }

    @ParameterizedTest
    @CsvSource({
        "SALE10, 900.0",
        "SALE20, 800.0",
        "HALF, 500.0"
    })
    void givenCartWithItems_whenApplyValidPromo_thenAppliesDiscount(
        final String promoCode,
        final double expectedTotal
    ) {
        val userId = "user_promo_" + promoCode;
        mockMvc.post()
            .uri(CART_ITEMS, userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"productId": "%s", "quantity": 2}
                """.formatted(PRODUCT_SMARTPHONE))
            .exchange();
        val result = mockMvc.post()
            .uri(CART_PROMO, userId)
            .param("code", promoCode)
            .exchange();

        assertThat(result).hasStatusOk()
            .bodyJson()
            .extractingPath("$.total")
            .isEqualTo(expectedTotal);
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "SALE99", " "})
    void givenInvalidPromo_whenApplyPromo_thenReturnsBadRequest(final String badPromo) {
        val result = mockMvc.post()
            .uri(CART_PROMO, "user_bad_promo")
            .param("code", badPromo)
            .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void givenItemInCart_whenRemovePartAndAll_thenUpdatesAndReturnsNotFoundAfter() {
        val userId = "user_remove";
        mockMvc.post()
            .uri(CART_ITEMS, userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"productId": "%s", "quantity": 2}
                """.formatted(PRODUCT_SMARTPHONE))
            .exchange();

        assertThat(mockMvc.delete()
            .uri(CART_ITEM_BY_ID, userId, PRODUCT_SMARTPHONE)
            .param("quantity", "1")
            .exchange())
            .hasStatusOk();
        assertThat(mockMvc.delete()
            .uri(CART_ITEM_BY_ID, userId, PRODUCT_SMARTPHONE)
            .exchange())
            .hasStatusOk();
        assertThat(mockMvc.delete()
            .uri(CART_ITEM_BY_ID, userId, PRODUCT_SMARTPHONE)
            .exchange())
            .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void givenUnknownProductOrExceedingStock_whenAddItem_thenReturnsBadRequest() {
        assertThat(mockMvc.post()
            .uri(CART_ITEMS, "user_err")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"productId": "unknown", "quantity": 1}
                """)
            .exchange())
            .hasStatus(HttpStatus.BAD_REQUEST);

        assertThat(mockMvc.post()
            .uri(CART_ITEMS, "user_err")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"productId": "%s", "quantity": 999}
                """.formatted(PRODUCT_SMARTPHONE))
            .exchange())
            .hasStatus(HttpStatus.BAD_REQUEST);
    }
}
