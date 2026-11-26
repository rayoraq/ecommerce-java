package com.shop.ecommerce.utils;

import com.shop.ecommerce.beans.Product;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ArgumentValidator {

    public static Product rejectDuplicate(
        @NonNull final Product first,
        @NonNull final Product second
    ) {

        throw new IllegalArgumentException("Дублирующийся id товара: %s".formatted(first.id()));
    }

    public static void requirePositive(final int quantity) {
        if (quantity <= 0) {

            throw new IllegalArgumentException("Количество должно быть больше 0: %s".formatted(quantity));
        }
    }
}
