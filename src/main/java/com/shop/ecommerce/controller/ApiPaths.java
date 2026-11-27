package com.shop.ecommerce.controller;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ApiPaths {
    public static final String CART_BASE = "/carts/{userId}";
    public static final String CART_ITEMS = CART_BASE + "/items";
    public static final String CART_ITEM_BY_ID = CART_ITEMS + "/{productId}";
    public static final String CART_PROMO = CART_BASE + "/promo";
}
