package com.shop.ecommerce.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public enum PromoCode {
    SALE10(10),
    SALE20(20),
    HALF(50),
    HOT(90);

    int discountPercent;
}
