package com.funride.exception;

/**
 * Thrown when JWT token has expired.
 */
public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {

        super(message);
    }
}