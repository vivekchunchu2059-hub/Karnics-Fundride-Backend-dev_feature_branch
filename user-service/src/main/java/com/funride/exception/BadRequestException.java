package com.funride.exception;

/**
 * Exception thrown when invalid request data is provided.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Creates bad request exception with message.
     *
     * @param message exception message
     */
    public BadRequestException(String message) {
        super(message);
    }
}
