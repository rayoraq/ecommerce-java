package com.shop;

public enum PromoCode {
    SALE10(10),
    SALE20(20),
    HALF(50),
    HOT(90);

    int discountPercent;

    PromoCode(int discount) {
        this.discountPercent = discount;
    }

    public int discountPercent() {
        return discountPercent;
    }
}
