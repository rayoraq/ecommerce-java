package com.shop.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddItemRequest(
    @NotBlank(message = "productId не может быть пустым") String productId,
    @Positive(message = "quantity должен быть больше 0") int quantity
) {
}