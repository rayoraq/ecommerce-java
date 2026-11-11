package com.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public enum PromoCode {
    SALE10(10),
    SALE20(20),
    HALF(50),
    HOT(90);

    int discountPercent;

    public static Optional<PromoCode> findByText(String text){
        return Optional.ofNullable(text)
            .map(String::trim)
            .map(String::toUpperCase)
            .flatMap(normalized -> Arrays.stream(values())
                .filter(code -> code.name().equals(normalized))
                .findFirst());
    }
}
