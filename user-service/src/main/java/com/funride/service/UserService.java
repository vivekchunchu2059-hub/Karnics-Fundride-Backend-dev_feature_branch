package com.funride.service;

import com.funride.dto.*;
import com.funride.entity.UserEntity;
import com.funride.response.ApiResponse;

import java.util.UUID;

/**
 * Service interface for managing user profile operations.
 */
public interface UserService {

    ApiResponse<UserEntity> createProfile(String phone ,CreateProfileRequestDto requestDto);
    ApiResponse<UserEntity> getUserProfile(String phone);
    ApiResponse<UserEntity> updateProfile(String phone, UpdateProfileRequestDto requestDto);
    ApiResponse<UserEntity> uploadKyc(String phone, UploadKycRequestDto requestDto);
    UserResponseDto getUserById(UUID userId);
    UserResponseDto getUserByPhone(String phone);
    ApiResponse<KycResponseDto> getKyc(String phone);
}
