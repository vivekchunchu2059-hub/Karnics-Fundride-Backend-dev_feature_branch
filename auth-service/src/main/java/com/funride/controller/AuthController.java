package com.funride.controller;

import com.funride.dto.*;
import com.funride.service.signupService;
import com.funride.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

/**
 * AUTH Controller responsible for handling authentication-related APIs
 * such as sending OTP, verifying OTP, and user signup.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final signupService signupService;

    /**
     * Generate and Sends OTP to the provided phone number.
     *
     * @param sendOtpRequestDTO request DTO containing user's phone number
     * @return API response containing OTP sending status
     */
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(
            @RequestBody sendOtpRequestDTO sendOtpRequestDTO) {
        log.info("Send OTP API called for phone={}", sendOtpRequestDTO.getPhone());

        ApiResponse response = signupService.sendOtp(sendOtpRequestDTO);
        log.info( "OTP Send successfully for phone={}", sendOtpRequestDTO.getPhone());

        return ResponseEntity.ok(response);
    }


    /**
     * Verifies the OTP entered by the user.
     *
     * @param verifyOtpRequestDTO request DTO containing phone number and OTP
     * @return VerifyOtpResponseDTO containing OTP verification status, token
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponseDTO> verifyOtp(
            @RequestBody verifyOtpRequestDTO verifyOtpRequestDTO) {
        log.info("Verify OTP API called for phone={}", verifyOtpRequestDTO.getPhone());

        VerifyOtpResponseDTO response = signupService.verifyOtp(verifyOtpRequestDTO);
        log.info("OTP has been verified successfully for phone={}", verifyOtpRequestDTO.getPhone());

        return ResponseEntity.ok(response);
    }

    /**
     * Generates a new access token using a valid refresh token.
     *
     * <p>
     * This endpoint is used when the current access token has expired.
     * The refresh token is validated and matched against the database.
     * If valid, a new access token is generated and returned to the client.
     * </p>
     *
     * @param requestDTO request DTO containing the refresh token
     * @return RefreshTokenResponseDTO containing newly generated access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(
            @RequestBody RefreshTokenRequestDTO requestDTO) {

        log.info("Refresh token API called");

        RefreshTokenResponseDTO response = signupService.refreshToken(requestDTO);
        log.info("New access token generated successfully using refresh token");

        return ResponseEntity.ok(response);

    }

    /**
     * Logout user and remove refresh token.
     *
     * @param phone authenticated user's phone
     * @return logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader("X-USER-PHONE")
            String phone) {

        log.info("Logout request received for phone={}", phone);
        ApiResponse response = signupService.logout(phone);
        log.info("Logout successful for phone={}", phone);
        return ResponseEntity.ok(response);
    }

}