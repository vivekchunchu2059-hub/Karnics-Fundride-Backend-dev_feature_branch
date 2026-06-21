package com.funride.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing user verification details.
 * Stores KYC verification statuses for each user.
 */
@Entity
@Table(name = "user_verifications")
@Data
@NoArgsConstructor
public class UserVerificationEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "aadhaar_verified")
    private Boolean aadhaarVerified = false;

    @Column(name = "driving_license_verified")
    private Boolean drivingLicenseVerified = false;

    @Column(name = "selfie_verified")
    private Boolean selfieVerified = false;

    @Column(name = "verification_status")
    private String verificationStatus = "pending";

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        if (aadhaarVerified == null) {
            aadhaarVerified = false;
        }

        if (drivingLicenseVerified == null) {
            drivingLicenseVerified = false;
        }

        if (selfieVerified == null) {
            selfieVerified = false;
        }

        if (verificationStatus == null) {
            verificationStatus = "pending";
        }

        createdAt = LocalDateTime.now();
    }
}