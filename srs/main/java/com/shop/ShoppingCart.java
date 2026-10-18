package com.shop;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<String, Product> catalog = new HashMap<>();
    private final Map<String, Integer> items = new LinkedHashMap<>();

    public ShoppingCart(Collection<Product> products) {
        for (Product p : products) {
            catalog.put(p.getId(), p);
        }
    }

    public Map<String, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void addItem(String productId, int quantity) {
        if (quantity <= 0) {
            throw new CartException("Количество должно быть больше 0");
        }
        Product product = catalog.get(productId);
        if (product == null) {
            throw new CartException("Товар не найден: " + productId);
        }
        int newQuantity = items.getOrDefault(productId, 0) + quantity;
        if (newQuantity > product.getStock()) {
            throw new CartException("На складе только " + product.getStock() + " шт.");
        }
        items.put(productId, newQuantity);
    }

    public void removeItem(String productId) {
        if (items.remove(productId) == null) {
            throw new CartException("Товара нет в корзине: " + productId);
        }
    }

    public void removeItem(String productId, int quantity) {
        if (quantity <= 0) {
            throw new CartException("Количество должно быть больше 0");
        }
        Integer current = items.get(productId);
        if (current == null) {
            throw new CartException("Товара нет в корзине: " + productId);
        }
        if (quantity >= current) {
            items.remove(productId);
        } else {
            items.put(productId, current - quantity);
        }
    }

    public double getTotal() {
        double sum = 0;
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            sum += catalog.get(e.getKey()).getPrice() * e.getValue();
        }
        return Math.round(sum * 100.0) / 100.0;
    }
}