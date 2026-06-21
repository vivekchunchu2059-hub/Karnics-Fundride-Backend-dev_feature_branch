package com.funride.controller;

import com.funride.dto.*;
import com.funride.entity.UserEntity;
import com.funride.response.ApiResponse;
import com.funride.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * User Controller responsible for handling user profile related APIs
 * such as creating profile, viewing profile, and updating profile.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user profile in the system.
     *
     * @param requestDto request DTO containing user profile details
     * @return API response containing profile creation status and created user data
     */
    @PostMapping("/create-profile")
    public ResponseEntity<ApiResponse<UserEntity>> createProfile(
            @RequestHeader("X-User-Phone") String phone,
            @RequestBody CreateProfileRequestDto requestDto) {

        log.info("Create profile API called");
        ApiResponse<UserEntity> response = userService.createProfile(phone, requestDto);
        log.info("Profile created successfully");
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves the current user's profile.
     *
     * @return API response containing user profile details
     */
    @GetMapping("/get-profile")
    public ResponseEntity<ApiResponse<UserEntity>> getProfile(
            @RequestHeader("X-User-Phone") String phone
    ) {

        log.info("Get profile API called");

        ApiResponse<UserEntity> response = userService.getUserProfile(phone);
        log.info("Profile fetched successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Updates the existing user profile.
     *
     * @param requestDto updated profile details
     * @return API response containing updated profile data
     */
    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<UserEntity>> updateProfile(
            @RequestHeader("X-User-Phone") String phone,
            @RequestBody UpdateProfileRequestDto requestDto) {

        log.info("Update profile API called");

        ApiResponse<UserEntity> response = userService.updateProfile(phone, requestDto);
        log.info("Profile updated successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Uploads user KYC documents such as Aadhaar and Driving License.
     *
     * If this is the first KYC upload for the user, an entry will also be
     * created in the user_verifications table with default verification status.
     *
     * @param phone authenticated user's phone number
     * @param requestDto contains KYC document details
     * @return API response containing updated user details
     */
    @PostMapping("/kyc/upload")
    public ResponseEntity<ApiResponse<UserEntity>> uploadKyc(
            @RequestHeader("X-User-Phone") String phone,
            @RequestBody UploadKycRequestDto requestDto) {

        log.info("Upload KYC API called for phone={}", phone);
        ApiResponse<UserEntity> response = userService.uploadKyc(phone, requestDto);

        log.info("KYC uploaded successfully for phone={}", phone);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetches KYC details of the logged-in user.
     *
     * @param phone user's phone number
     * @return KYC details
     */
    @GetMapping("/kyc/status")
    public ResponseEntity<ApiResponse<KycResponseDto>> getKyc(
            @RequestHeader("X-User-Phone") String phone) {

        log.info("Get KYC API called for phone={}", phone);
        ApiResponse<KycResponseDto> response = userService.getKyc(phone);

        log.info("KYC fetched successfully for phone={}", phone);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetches user details using phone number for ride-service.
     *
     */
    @GetMapping("/{phone}")
    public ResponseEntity<UserResponseDto> getUserByPhone(
            @PathVariable String phone) {

        log.info("Fetching user details by phone={}", phone);

        return ResponseEntity.ok(userService.getUserByPhone(phone)
        );
    }

    /**
     * Fetches user details using user id for booking.
     */
    @GetMapping("/id/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable UUID userId) {

        log.info("Fetching user details by userId={}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
