package com.funride.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response format class used for sending
 * success status and message to client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    /**
     * Indicates whether API request was successful.
     */
    private boolean success;

    /**
     * Response message returned to client.
     */
    private String message;
}