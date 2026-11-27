package com.shop.ecommerce.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record CartResponse(
    String userId,
    Map<String, Integer> items,
    int discountPercent,
    double total
) {
}