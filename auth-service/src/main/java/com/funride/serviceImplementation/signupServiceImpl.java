package com.funride.serviceImplementation;

import com.funride.dto.*;
import com.funride.entity.signupEntity;
import com.funride.repository.signupRepository;
import com.funride.service.signupService;
import com.funride.util.OtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;
import com.funride.exception.BadRequestException;
import com.funride.response.ApiResponse;
import com.funride.security.JwtUtil;
import com.funride.entity.RefreshTokenEntity;
import com.funride.repository.RefreshTokenRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.time.Duration;
import java.util.Optional;
import java.util.Objects;

import static com.funride.constants.AppConstant.*;

/**
 * Service implementation for handling OTP generation,
 * OTP verification, and user signup operations.
 */
@Slf4j
@Service
public class signupServiceImpl implements signupService {

    private final signupRepository signupRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public signupServiceImpl(signupRepository signupRepository) {
        this.signupRepository = signupRepository;
    }

    /**
     * Generates and sends OTP to the provided phone number.
     * OTP is stored in Redis with expiration time.
     *
     * @param sendOtpRequestDTO request DTO containing phone number
     * @return API response containing OTP sending status
     */
    @Override
    public ApiResponse sendOtp(sendOtpRequestDTO sendOtpRequestDTO) {
        String phone = sendOtpRequestDTO.getPhone();
        String flowType = sendOtpRequestDTO.getFlowType();

        log.info("Send OTP request received for phone={} and flowType={}", phone, flowType);

        // Validate phone number
        if (phone == null || !phone.matches("^[0-9]{10}$")) {
            log.warn("Invalid phone number received: {}", phone);
            throw new BadRequestException(
                messageSource.getMessage(
                    "error.invalid.phone",
                    null,
                    Locale.getDefault()
                )
            );
        }

        // Check user exists in DB
        Optional<signupEntity> existingUser =
                signupRepository.findByPhone(phone);

        if (flowType == null || flowType.isBlank()) {
            throw new BadRequestException(
                messageSource.getMessage(
                    "error.flow.type.required",
                    null,
                    Locale.getDefault()
                )
            );
        }
        flowType = flowType.trim().toUpperCase(Locale.ROOT);

        /**
         * LOGIN FLOW
         */
        if (Objects.equals(flowType, LOGIN)) {
            // User not found
            if (existingUser.isEmpty()) {
                log.warn("Login attempted with unregistered phone={}",phone);

                throw new BadRequestException(
                    messageSource.getMessage(
                        "error.phone.not.registered",
                        null,
                        Locale.getDefault()
                    )
                );
            }
        }

        /**
         * SIGNUP FLOW
         */
        else if (Objects.equals(flowType, SIGNUP)) {

            // User already exists
            if (existingUser.isPresent()) {
                log.warn("Signup attempted with existing phone={}", phone);

                throw new BadRequestException(
                    messageSource.getMessage(
                        "error.phone.registered",
                        null,
                        Locale.getDefault()
                    )
                );
            }
        }
        else {
            throw new BadRequestException(
                messageSource.getMessage(
                    "error.flow.type.invalid",
                    null,
                    Locale.getDefault()
                )
            );
        }

        // Generate OTP
        String otp = otpUtil.generateOtp();

        // Save OTP in Redis
        redisTemplate.opsForValue().set(
                OTP_PREFIX + phone,
                otp,
                Duration.ofMinutes(5)
        );

        System.out.println(otp);
        log.info("OTP generated and stored successfully for otp={}", otp);
         return new ApiResponse(
            true,
                 OTP_SENT_SUCCESSFULLY
         );
    }

    /**
     * Verifies user OTP stored in Redis.
     * Creates partial user record after successful verification.
     *
     * @param verifyOtpRequestDTO request DTO containing phone number and OTP
     * @return VerifyOtpResponseDTO containing OTP verification status
     */
    @Override
    @Transactional
    public VerifyOtpResponseDTO verifyOtp(verifyOtpRequestDTO verifyOtpRequestDTO) {
        String phone = verifyOtpRequestDTO.getPhone();

        log.info("Verify OTP request received for phone={}", phone);

        String enteredOtp = verifyOtpRequestDTO.getOtp();

        // Fetch OTP from Redis
        String storedOtp = redisTemplate.opsForValue()
                        .get(OTP_PREFIX + phone);

        System.out.println(storedOtp + " stored otp");
        // OTP expired
        if (storedOtp == null) {
            log.warn("OTP expired or not found for phone={}", phone);
            throw new BadRequestException(
                messageSource.getMessage(
                "error.otp.expired",
                null,
                Locale.getDefault()
                )
            );
        }

        // Invalid OTP
        if (!storedOtp.equals(enteredOtp)) {
            log.warn("Invalid OTP entered for phone={}", phone);
            throw new BadRequestException(
                messageSource.getMessage(
                "error.invalid.otp",
                null,
                Locale.getDefault()
                )
            );
        }

        // Check if user exists
        Optional<signupEntity> existingUser = signupRepository.findByPhone(phone);

            signupEntity user;
            boolean isNewUser = false;

        if (existingUser.isEmpty()) {
            user = new signupEntity();
            user.setPhone(phone);
            user.setIsPhoneVerified(true);
            user.setProfileCompleted(false);
            signupRepository.save(user);
            isNewUser = true;
        } else {
            user = existingUser.get();
        }

       /**
        * Generate JWT access token and refresh token
        */
        String accessToken = jwtUtil.generateAccessToken(phone);
        String refreshToken = jwtUtil.generateRefreshToken(phone);

        refreshTokenRepository.deleteByPhone(phone);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setPhone(phone);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiryDate(LocalDateTime.now().plusDays(30));

        refreshTokenRepository.save(refreshTokenEntity);

        // Remove OTP only after full success (so JWT errors do not consume the OTP)
        redisTemplate.delete(OTP_PREFIX + phone);

        log.info("OTP verification completed successfully for phone={}", phone);

        return new VerifyOtpResponseDTO(
            true,
            OTP_VERIFIED,
            accessToken,
            refreshToken,
            user.getProfileCompleted(),
            isNewUser
        );


    }

    /**
     * Validates the refresh token and generates a new access token.
     *
     * @param requestDTO request DTO containing refresh token
     * @return response DTO containing newly generated access token
     */
    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO) {

        log.info("Refresh token request received");

        String refreshToken = requestDTO.getRefreshToken();
        Optional<RefreshTokenEntity> tokenRecord = refreshTokenRepository.findByToken(refreshToken);

        if (tokenRecord.isEmpty()) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.empty.refresh.token",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        boolean isValid = jwtUtil.validateToken(refreshToken);

        if (!isValid) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.invalid.refresh.token",
                            null,
                            Locale.getDefault()
                    )
            );
        }
        String phone = jwtUtil.extractPhone(refreshToken);

        log.info("Refresh token validated successfully for phone={}", phone);

        String newAccessToken = jwtUtil.generateAccessToken(phone);
        log.info("New access token generated successfully for phone={}", phone);

        return new RefreshTokenResponseDTO(
                true,
                "Access token generated successfully",
                newAccessToken
        );
    }


    /**
     * Logs out the authenticated user.
     *
     * <p>
     * The logout operation removes the user's refresh token
     * from the database, preventing future access token
     * generation through the refresh token flow.
     * </p>
     *
     * @param phone authenticated user's phone number
     * @return API response indicating logout success
     */
    @Override
    @Transactional
    public ApiResponse logout(String phone) {
        log.info("Processing logout for phone={}", phone);

        signupEntity user = signupRepository.findByPhone(phone)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        messageSource.getMessage(
                                                "error.user.not.found",
                                                null,
                                                Locale.getDefault()
                                        )
                                )
                        );

        refreshTokenRepository.deleteByPhone(phone);

        log.info("Refresh token deleted successfully for phone={}", phone);

        return new ApiResponse(
                true,
                "Logout successful"
        );
    }

}