package com.funride.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response format class used for sending
 * success status, message, and response data to client.
 *
 * @param <T> type of response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * Indicates whether API request was successful.
     */
    private boolean success;

    /**
     * Response message returned to client.
     */
    private String message;

    /**
     * Actual response data returned to client.
     */
    private T data;

    /**
     * Constructor for responses without data.
     *
     * @param success success status
     * @param message response message
     */
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
