package com.funride.dto;

import lombok.Data;

/**
 * Request DTO used for generating a new access token.
 */
@Data
public class RefreshTokenRequestDTO {

    /**
     * User refresh token.
     */
    private String refreshToken;
}