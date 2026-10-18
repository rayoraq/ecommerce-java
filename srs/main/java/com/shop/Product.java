package com.shop;

public class Product {
    private final String id;
    private final String name;
    private final double price;
    private final int stock;

    public Product(String id, String name, double price, int stock) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id должен быть непустым");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name должен быть непустым");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price не может быть отрицательной");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("stock не может быть отрицательным");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
}