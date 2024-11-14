package com.arista.nestnavigator.custom_exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
