package com.example.mini_market.Exceptions;

public class EmptyInputException extends RuntimeException {
    public EmptyInputException(String fieldName) {
        super("The field '" + fieldName + "' cannot be empty.");
    }
}
