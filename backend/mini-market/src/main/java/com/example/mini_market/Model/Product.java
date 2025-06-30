package com.example.mini_market.Model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal vat;
    private int categoryId;
    private String barcode;
    private boolean active;


    public Product() {}

    public Product(int id, String name, int quantity, BigDecimal price, BigDecimal vat, int categoryId,  String barcode, boolean active) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.vat = vat;
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
