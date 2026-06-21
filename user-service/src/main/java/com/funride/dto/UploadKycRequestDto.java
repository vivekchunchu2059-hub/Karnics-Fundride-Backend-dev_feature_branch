package com.funride.dto;

import lombok.Data;

/**
 * DTO used to receive KYC document upload requests.
 */
@Data
public class UploadKycRequestDto {
    private String aadhaarDocument;
    private String licenseDocument;
}