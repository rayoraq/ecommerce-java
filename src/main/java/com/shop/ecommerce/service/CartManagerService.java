package com.shop.ecommerce.service;

import com.shop.ecommerce.beans.Product;
import com.shop.ecommerce.utils.ArgumentValidator;
import com.shop.ecommerce.utils.BeanValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartManagerService {
    @Getter
    @NonNull Map<String, Product> catalog;
    @NonNull Map<String, ShoppingCartService> userCarts = new ConcurrentHashMap<>();

    public CartManagerService() {
        Set<Product> initialProducts = Set.of(
            Product.builder()
                .id("prod_1")
                .name("Смартфон")
                .price(500.0)
                .stock(10)
                .build(),
            Product.builder()
                .id("prod_2")
                .name("Чехол")
                .price(10.5)
                .stock(100)
                .build(),
            Product.builder()
                .id("prod_3")
                .name("Ноутбук")
                .price(1200.0)
                .stock(5)
                .build()
        );
        this.catalog = initialProducts.stream()
            .map(BeanValidator::validate)
            .collect(toUnmodifiableMap(
                Product::id,
                identity(),
                ArgumentValidator::rejectDuplicate
            ));
    }

    public ShoppingCartService getOrCreateCart(@NonNull final String userId) {
        return userCarts.computeIfAbsent(userId, id -> new ShoppingCartService(Set.copyOf(catalog.values())));
    }
}
