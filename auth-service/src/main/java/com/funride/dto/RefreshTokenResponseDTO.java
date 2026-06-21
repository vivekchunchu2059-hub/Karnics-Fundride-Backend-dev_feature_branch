package com.funride.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO returned after successful refresh token validation.
 * Contains a newly generated access token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponseDTO {

    private boolean success;
    private String message;

    /**
     * Newly generated access token.
     */
    private String accessToken;
}