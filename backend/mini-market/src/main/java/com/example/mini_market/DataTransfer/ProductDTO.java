package com.example.mini_market.DataTransfer;

public class ProductDTO {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private double vat;
    private String barcode;

    private SimpleEntity category;
  

    public ProductDTO(int id, String name, int quantity, double price, double vat, String barcode,
                      SimpleEntity category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.vat = vat;
        this.barcode = barcode;
        this.category = category;
       
    }

    // Nested helper class
    public static class SimpleEntity {
        private int id;
        private String name;

        public SimpleEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

     public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public SimpleEntity getCategory() {
        return category;
    }

    public void setCategory(SimpleEntity category) {
        this.category = category;
    }

}
