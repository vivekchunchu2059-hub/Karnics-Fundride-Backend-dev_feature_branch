package com.funride.dto;
import lombok.Data;

/**
 * DTO used for OTP verification request.
 * Contains phone number and OTP entered by the user.
 */
@Data
public class verifyOtpRequestDTO {
    private String phone;
    private String otp;
}