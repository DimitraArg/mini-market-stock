package com.example.mini_market.DataTransfer;

import java.time.LocalDateTime;

public class TransactionDTO {
    private int id;
    private String type;
    private int quantity;
    private LocalDateTime timestamp;

    private ProductSummary product;
    private ProductDTO.SimpleEntity supplier;

    public TransactionDTO() {
    }

    public TransactionDTO(int id, String type, int quantity, LocalDateTime timestamp,
                          ProductSummary product, ProductDTO.SimpleEntity supplier) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.product = product;
        this.supplier = supplier;
    }

    // Nested helper class
    public static class ProductSummary {
        private int id;
        private String name;
        private String barcode;
        private double price;
        private double vat;
        private ProductDTO.SimpleEntity supplier;

        public ProductSummary(int id, String name, String barcode, double price, double vat, ProductDTO.SimpleEntity supplier) {
            this.id = id;
            this.name = name;
            this.barcode = barcode;
            this.price = price;
            this.vat = vat;
            this.supplier = supplier;
        }

        public int getId() {
             return id; 
        }
        
        public String getName() { 
            return name;
        }
        public String getBarcode() { 
            return barcode;
        }

        public double getPrice() { 
            return price; 
        }

        public double getVat() { 
            return vat; 
        }

        public ProductDTO.SimpleEntity getSupplier() { 
            return supplier; 
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ProductSummary getProduct() {
        return product;
    }

    public void setProduct(ProductSummary product) {
        this.product = product;
    }

    public ProductDTO.SimpleEntity getSupplier() {
        return supplier;
    }

    public void setSupplier(ProductDTO.SimpleEntity supplier) {
        this.supplier = supplier;
    }
}
