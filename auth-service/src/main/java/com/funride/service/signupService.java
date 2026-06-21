package com.funride.service;
import com.funride.dto.*;

import com.funride.response.ApiResponse;

/**
 * Service interface for handling OTP verification
 * and user signup operations.
 */
public interface signupService {

    ApiResponse sendOtp(sendOtpRequestDTO sendOtpRequestDTO);
    VerifyOtpResponseDTO verifyOtp(verifyOtpRequestDTO verifyOtpRequestDTO);
    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO);
    ApiResponse logout(String phone);
}