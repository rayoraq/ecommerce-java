package com.shop;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public enum PromoCode {
    SALE10(10),
    SALE20(20),
    HALF(50),
    HOT(90);

    private static final Map<String, PromoCode> NAME = Arrays.stream(values())
        .collect(toUnmodifiableMap(PromoCode::name, identity()));

    int discountPercent;

    @Nullable
    public static PromoCode findByText(@NonNull final String text) {
        return NAME.get(text.trim().toUpperCase());
    }
}
