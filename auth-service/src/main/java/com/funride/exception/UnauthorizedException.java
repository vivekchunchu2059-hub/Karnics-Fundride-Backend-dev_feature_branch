package com.funride.exception;

/**
 * Exception thrown when user is unauthorized
 */
public class UnauthorizedException
        extends RuntimeException {

    /**
     * Creates unauthorized exception with message.
     *
     * @param message exception message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}