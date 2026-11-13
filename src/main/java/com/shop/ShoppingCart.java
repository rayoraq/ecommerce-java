package com.shop;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.shop.PromoCode.findByText;
import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

@FieldDefaults(makeFinal = true)
public class ShoppingCart {
    Map<String, Product> catalog;
    Map<String, Integer> items = new LinkedHashMap<>();

    @Getter
    @NonFinal
    int discountPercent;

    public ShoppingCart(@NonNull final Set<Product> products) {
        catalog = products.stream()
            .collect(toUnmodifiableMap(Product::id, identity(), ShoppingCart::rejectDuplicate));
    }

    private static Product rejectDuplicate(final Product first, final Product second) {
        throw new IllegalArgumentException("Дублирующийся id товара: %s".formatted(first.id()));
    }

    public Map<String, Integer> getItems() {
        return unmodifiableMap(items);
    }

    public boolean addItem(@NonNull final String productId, final int quantity) {
        requirePositive(quantity);
        val newQuantity = items.getOrDefault(productId, 0) + quantity;
        if (newQuantity > findProduct(productId).stock()) {
            return false;
        }
        items.put(productId, newQuantity);
        return true;
    }

    public boolean removeItem(@NonNull final String productId) {
        return items.remove(productId) != null;
    }

    public boolean removeItem(@NonNull final String productId, final int quantity) {
        requirePositive(quantity);
        val current = items.get(productId);
        if (current == null) {
            return false;
        }
        if (quantity >= current) {
            items.remove(productId);
        } else {
            items.put(productId, current - quantity);
        }
        return true;
    }

    public boolean applyPromoCode(@NonNull final String text) {
        val promoCode = findByText(text);
        if (promoCode == null) {
            return false;
        }
        discountPercent = promoCode.getDiscountPercent();
        return true;
    }

    public double getTotal() {
        val sum = items.entrySet()
            .stream()
            .mapToDouble(entry -> catalog.get(entry.getKey()).price() * entry.getValue())
            .sum();
        return Math.round(sum * (100 - discountPercent)) / 100.0;
    }

    private static void requirePositive(final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше 0: " + quantity);
        }
    }

    private Product findProduct(final String productId) {
        return ofNullable(catalog.get(productId))
            .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + productId));
    }
}