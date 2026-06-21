package com.funride.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO returned after successful OTP verification.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpResponseDTO {

   
    private boolean success;
    private String message;

    /**
     * JWT authentication token.
     */
    private String accessToken;

    private String refreshToken;

    /**
     * Indicates whether profile is completed.
     */
    private Boolean profileCompleted;

    /**
     * Indicates whether user is newly registered.
     */
    private Boolean isNewUser;
}