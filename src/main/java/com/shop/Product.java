package com.shop;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public record Product(@NonNull String id, @NonNull String name, double price, int stock) {

    public Product {
        requireNotBlank(id, "id");
        requireNotBlank(name, "name");
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("price должна быть конечной и неотрицательной: %s".formatted(price));
        }
        if (stock < 0) {
            throw new IllegalArgumentException("stock не может быть отрицательным: %s".formatted(stock));
        }
    }

    private static void requireNotBlank(final String value, final String field) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("%s должен быть непустым".formatted(field));
        }
    }
}