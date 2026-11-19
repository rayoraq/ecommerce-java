package com.shop;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Product(
    @NonNull
    @NotBlank(message = "id не может быть пустым")
    String id,

    @NonNull
    @NotBlank(message = "name не может быть пустым")
    String name,

    @PositiveOrZero(message = "price не может быть отрицательной: ${validatedValue}")
    double price,

    @PositiveOrZero(message = "stock не может быть отрицательной: ${validatedValue}")
    int stock
) {

    @AssertTrue(message = "price должна быть конечной")
    public boolean isPriceFinite() {
        return Double.isFinite(price);
    }
}