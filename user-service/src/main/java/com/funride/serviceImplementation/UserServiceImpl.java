package com.funride.serviceImplementation;

import com.funride.dto.*;
import com.funride.entity.UserEntity;
import com.funride.entity.UserVerificationEntity;
import com.funride.exception.BadRequestException;
import com.funride.exception.ResourceNotFoundException;
import com.funride.repository.UserRepository;
import com.funride.repository.UserVerificationRepository;
import com.funride.response.ApiResponse;
import com.funride.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

import static com.funride.constants.AppConstant.*;

/**
 * Service implementation for handling user profile operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    private final UserVerificationRepository userVerificationRepository;

    /**
     * Creates a new user profile and stores it in the database.
     *
     * @param requestDto contains profile details received from frontend
     * @return API response containing profile creation status and created user data
     */
    @Override
    public ApiResponse<UserEntity> createProfile(
            String phone,
            CreateProfileRequestDto requestDto) {

        log.info("Create profile request received for phone={}", phone);

        // Find existing user
        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() -> {

                    log.warn("User not found for phone={}", phone);

                    return new ResourceNotFoundException(
                            messageSource.getMessage(
                                    "error.user.not.found",
                                    null,
                                    Locale.getDefault()
                            )

                    );
                });

        // Email duplicate check
        if (requestDto.getEmail() != null
                && userRepository.existsByEmail(requestDto.getEmail())
                && !requestDto.getEmail().equals(user.getEmail())) {

            log.warn("Email already exists: {}", requestDto.getEmail());

            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.email.exists",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        if (Boolean.TRUE.equals(user.getProfileCompleted())) {

        log.warn("Profile already exists for phone={}", phone);

            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.profile.already.exists",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        if(requestDto.getProfilePhoto() != null
                && requestDto.getProfilePhoto().length() > 5_000_000) {

            throw new BadRequestException( messageSource.getMessage(
                    "error.profile.photo.size.exceeded",
                    null,
                    Locale.getDefault()
            ));
        }

        if (requestDto.getAadhaarNumber() != null &&
                !requestDto.getAadhaarNumber().matches(AADHAAR_REGEX)) {

            throw new BadRequestException(messageSource.getMessage(
                    "error.invalid.aadhaar.number",
                    null,
                    Locale.getDefault()
            )
            );
        }

        if (requestDto.getAadhaarNumber() != null
                && userRepository.existsByAadhaarNumber(requestDto.getAadhaarNumber())) {

            throw new BadRequestException(messageSource.getMessage(
                    "error.aadhaar.number.already.exists",
                    null,
                    Locale.getDefault()
            ));
        }

        // Update existing user profile
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setGender(requestDto.getGender());
        user.setProfilePhoto(requestDto.getProfilePhoto());
        user.setDateOfBirth(requestDto.getDateOfBirth());
        user.setState(requestDto.getState());
        user.setCity(requestDto.getCity());
        user.setPinCode(requestDto.getPinCode());
        user.setAddress(requestDto.getAddress());
        user.setAadhaarNumber(requestDto.getAadhaarNumber());

        // Mark profile completed
        user.setProfileCompleted(true);

        UserEntity savedUser = userRepository.save(user);

        log.info("Profile created successfully for phone={}", phone);

        return new ApiResponse<>(
                true,
                PROFILE_CREATED_SUCCESSFULLY,
                savedUser
        );
    }

    /**
     * Fetches logged-in user's profile details.
     *
     * @return API response containing user profile data
     */
    @Override
    public ApiResponse<UserEntity> getUserProfile( String phone ) {

        log.info("Fetching profile for phone={}", phone);

        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() -> {

                    log.warn("User not found for phone={}", phone);

                    return new ResourceNotFoundException(
                            messageSource.getMessage(
                                    "error.user.not.found",
                                    null,
                                    Locale.getDefault()
                            )
                    );
                });

        log.info("Profile fetched successfully for phone={}", phone);

        return new ApiResponse<>(
                true,
                PROFILE_FETCHED_SUCCESSFULLY,
                user
        );
    }

    /**
     * Updates existing user's profile details.
     *
     * @param requestDto updated profile data
     * @return API response containing updated user data
     */
    @Override
    public ApiResponse<UserEntity> updateProfile(
            String phone,
            UpdateProfileRequestDto requestDto) {

        log.info("Update profile request received for phone={}", phone);

        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() -> {

                    log.warn("User not found for phone={}", phone);

                    return new ResourceNotFoundException(
                            messageSource.getMessage(
                                    "error.user.not.found",
                                    null,
                                    Locale.getDefault()
                            )
                    );
                });

        // Email duplicate check
        if (requestDto.getEmail() != null
                && userRepository.existsByEmail(requestDto.getEmail())
                && !requestDto.getEmail().equals(user.getEmail())) {

            log.warn("Email already exists: {}", requestDto.getEmail());

            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.email.exists",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        if(requestDto.getProfilePhoto() != null
                && requestDto.getProfilePhoto().length() > 5_000_000) {

            throw new BadRequestException( messageSource.getMessage(
                    "error.profile.photo.size.exceeded",
                    null,
                    Locale.getDefault()
            ));
        }

        if (requestDto.getAadhaarNumber() != null
                && !requestDto.getAadhaarNumber().matches(AADHAAR_REGEX)) {
            throw new BadRequestException( messageSource.getMessage(
                    "error.invalid.aadhaar.number",
                    null,
                    Locale.getDefault()
            ));
        }

        // Update profile fields
        if (requestDto.getFirstName() != null) {user.setFirstName(requestDto.getFirstName());}
        if (requestDto.getLastName() != null) {user.setLastName(requestDto.getLastName());}
        if (requestDto.getEmail() != null) {user.setEmail(requestDto.getEmail());}
        if (requestDto.getGender() != null) {user.setGender(requestDto.getGender());}
        if (requestDto.getDateOfBirth() != null) {user.setDateOfBirth(requestDto.getDateOfBirth());}
        if (requestDto.getState() != null) {user.setState(requestDto.getState());}
        if (requestDto.getCity() != null) {user.setCity(requestDto.getCity());}
        if (requestDto.getPinCode() != null) {user.setPinCode(requestDto.getPinCode());}
        if (requestDto.getAddress() != null) {user.setAddress(requestDto.getAddress());}
        if (requestDto.getProfilePhoto() != null) {user.setProfilePhoto(requestDto.getProfilePhoto());}
        user.setAadhaarNumber(requestDto.getAadhaarNumber());

        UserEntity updatedUser = userRepository.save(user);

        log.info("Profile updated successfully for phone={}", phone);

        return new ApiResponse<>(
                true,
                PROFILE_UPDATED_SUCCESSFULLY,
                updatedUser
        );
    }

    /**
     * Uploads KYC documents for the authenticated user.
     *
     * If verification details do not exist, a new verification
     * record is created with default pending status.
     *
     * @param phone authenticated user's phone number
     * @param requestDto KYC upload request
     * @return updated user information
     */
    @Override
    public ApiResponse<UserEntity> uploadKyc(String phone, UploadKycRequestDto requestDto) {

        log.info("Upload KYC request received for phone={}", phone);

        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage(
                                "error.user.not.found",
                                null,
                                Locale.getDefault()
                        )
                ));

        user.setAadhaarDocument(requestDto.getAadhaarDocument());
        user.setLicenseDocument(requestDto.getLicenseDocument());

        UserEntity updatedUser = userRepository.save(user);
        log.info("KYC documents saved successfully for userId={}", updatedUser.getId());

        boolean documentUploaded = requestDto.getAadhaarDocument() != null || requestDto.getLicenseDocument() != null;
        if (documentUploaded) {
            boolean verificationExists = userVerificationRepository.findByUserId(updatedUser.getId()).isPresent();

            if (!verificationExists) {
                UserVerificationEntity verification = new UserVerificationEntity();
                verification.setUser(updatedUser);

                userVerificationRepository.save(verification);
                log.info("Verification entry created for userId={}", updatedUser.getId());

            } else {
                log.debug("Verification entry already exists for userId={}", updatedUser.getId());
            }
        }

        log.info("KYC uploaded successfully for phone={}", phone);

        return new ApiResponse<>(
                true,
                "KYC uploaded successfully",
                updatedUser
        );
    }

    /**
     * Fetches KYC details of the user.
     *
     * @param phone user's phone number
     * @return KYC information
     */
    @Override
    public ApiResponse<KycResponseDto> getKyc(String phone) {

        log.info("Fetching KYC details for phone={}", phone);

        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "error.user.not.found",
                                        null,
                                        Locale.getDefault()
                                )
                )
                );

        UserVerificationEntity verification = userVerificationRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "error.kyc.status.not.found",
                                        null,
                                        Locale.getDefault()
                                )
                                )
                        );

        KycResponseDto responseDto = new KycResponseDto();

        responseDto.setAadhaarVerified(verification.getAadhaarVerified());
        responseDto.setDrivingLicenseVerified(verification.getDrivingLicenseVerified());
        responseDto.setSelfieVerified(verification.getSelfieVerified());
        responseDto.setVerificationStatus(verification.getVerificationStatus());
        responseDto.setVerifiedAt(verification.getVerifiedAt());

        log.info("KYC details fetched successfully for userId={}", user.getId());

        return new ApiResponse<>(
                true,
                KYC_STATUS_FETCHED_SUCCESSFULLY,
                responseDto
        );
    }

    /**
     * Fetches user details using phone number.
     *
     * @param phone registered phone number
     * @return user response dto
     */
    @Override
    public UserResponseDto getUserByPhone(String phone) {

        UserEntity user =
                userRepository.findByPhone(phone)
                        .orElseThrow(() -> {
                            log.warn("User not found for phone={}", phone);

                            return new ResourceNotFoundException(
                                    messageSource.getMessage(
                                            "error.user.not.found",
                                            null,
                                            Locale.getDefault()
                                    )
                            );
                        });

        log.debug("User details fetched successfully for phone={}", phone);
        return mapToUserResponseDto(user);
    }

    /**
     * Fetches user details using user id.
     *
     * @param userId unique user identifier
     * @return user response dto
     */
    @Override
    public UserResponseDto getUserById(UUID userId) {
        log.info("Fetching user details for userId={}", userId);

        UserEntity user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {
                            return new ResourceNotFoundException(
                                    messageSource.getMessage(
                                            "error.user.not.found",
                                            null,
                                            Locale.getDefault()
                                    )
                            );
                        });

        log.debug("User details fetched successfully for userId={}", userId);
        return mapToUserResponseDto(user);
    }

    /**
     * Converts UserEntity into UserResponseDto.
     *
     * @param user user entity
     * @return mapped response dto
     */
    private UserResponseDto mapToUserResponseDto(
            UserEntity user) {

        return new UserResponseDto(
                user.getId(),
                user.getPhone(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePhoto(),
                user.getTrustScore(),
                user.getProfileCompleted()
        );
    }
}


