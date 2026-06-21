package com.funride.exception;

/**
 * Thrown when JWT token is invalid.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {

        super(message);
    }
}