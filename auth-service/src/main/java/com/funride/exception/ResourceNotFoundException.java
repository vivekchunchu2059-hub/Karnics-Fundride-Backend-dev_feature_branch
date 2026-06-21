package com.funride.exception;

/**
 * Exception thrown when requested resource is not found.
 */
public class ResourceNotFoundException
        extends RuntimeException {

    /**
     * Creates resource not found exception with message.
     *
     * @param message exception message
     */

    public ResourceNotFoundException(String message) {
        super(message);
    }
}