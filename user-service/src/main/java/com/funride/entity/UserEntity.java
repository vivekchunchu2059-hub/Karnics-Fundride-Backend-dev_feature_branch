package com.funride.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity to represent the user table in DB.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "profile_photo", columnDefinition = "TEXT")
    private String profilePhoto;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "role")
    private String role;

    @Column(name = "trust_score")
    private Integer trustScore;

    @Column(name = "is_phone_verified")
    private Boolean isPhoneVerified;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "aadhaar_document", columnDefinition = "TEXT")
    private String aadhaarDocument;

    @Column(name = "aadhaar_number", unique = true, length = 12)
    private String aadhaarNumber;

    @Column(name = "license_document", columnDefinition = "TEXT")
    private String licenseDocument;

    @Column(name = "profile_completed")
    private Boolean profileCompleted = false;

    @Column(name = "is_profile_verified")
    private Boolean isProfileVerified = false;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

        if (this.role == null) {
            this.role = "BOTH";
        }

        if (this.trustScore == null) {
            this.trustScore = 0;
        }

        if (this.isBlocked == null) {
            this.isBlocked = false;
        }

        if (this.isEmailVerified == null) {
            this.isEmailVerified = false;
        }

        if (this.isPhoneVerified == null) {
            this.isPhoneVerified = false;
        }

        if (this.profileCompleted == null) {
            this.profileCompleted = false;
        }

        if (this.isProfileVerified == null) {
            this.isProfileVerified = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}