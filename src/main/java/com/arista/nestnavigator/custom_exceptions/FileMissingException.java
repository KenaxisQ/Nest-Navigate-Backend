package com.arista.nestnavigator.custom_exceptions;

public class FileMissingException extends RuntimeException {
    public FileMissingException(String message) {
        super(message);
    }
}