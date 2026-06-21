package com.funride.dto;
import lombok.Data;

/**
 * DTO used for sending OTP request.
 * Contains user's phone number.
 */
@Data
public class sendOtpRequestDTO {
    private String phone;
    /**
     * Flow type for OTP request: LOGIN or SIGNUP.
     */
    private String flowType;
}
