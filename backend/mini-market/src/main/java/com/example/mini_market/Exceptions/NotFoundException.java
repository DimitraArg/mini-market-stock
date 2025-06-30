package com.example.mini_market.Exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, String fieldName, String value) {
        super(entityName + " with " + fieldName + " '" + value + "' not found.");
    }

    public NotFoundException(String entityName, int id) {
        super(entityName + " with ID " + id + " not found.");
    }

    public NotFoundException(String entityName, String barcode) {
        super(entityName + " with barcode " + barcode + " not found.");
    }
}
