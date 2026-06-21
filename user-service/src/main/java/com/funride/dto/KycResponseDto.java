package com.funride.dto;

import lombok.Data;

import java.time.LocalDateTime;
/**
 * DTO used to get KYC document verified status.
 */
@Data
public class KycResponseDto {

    private Boolean aadhaarVerified;
    private Boolean drivingLicenseVerified;
    private Boolean selfieVerified;

    private String verificationStatus;

    private LocalDateTime verifiedAt;
}