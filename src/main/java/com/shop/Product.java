package com.shop;

import lombok.NonNull;

public record Product(@NonNull String id, @NonNull String name, double price, int stock) {

    public Product {
        if (id.isBlank()) {
            throw new IllegalArgumentException("id должен быть непустым");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("name должен быть непустым");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price не может быть отрицательной");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("stock не может быть отрицательным");
        }
    }
}