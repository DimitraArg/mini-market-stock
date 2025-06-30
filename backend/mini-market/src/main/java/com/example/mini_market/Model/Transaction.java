package com.example.mini_market.Model;

import java.time.LocalDateTime;

public class Transaction {

    private int id;
    private int productId;
    private String type;
    private int quantity;
    private LocalDateTime timestamp;

    private Product product;
    private int supplierId;
    private Supplier supplier;

    public Transaction() {
    }

    public Transaction(int id, int productId, int supplier_Id, Supplier supplier, String type, int quantity, LocalDateTime timestamp) {
        this.id = id;
        this.productId = productId;
        this.supplierId = supplier_Id;
        this.supplier = supplier;
        this.type = type;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    
}
