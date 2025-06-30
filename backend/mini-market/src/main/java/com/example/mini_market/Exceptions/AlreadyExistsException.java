package com.example.mini_market.Exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String entityName, String fieldName, String value) {
        super(entityName + " with " + fieldName + " '" + value + "' already exists.");
    }

}
