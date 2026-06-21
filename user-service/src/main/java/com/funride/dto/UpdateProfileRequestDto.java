package com.funride.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for updating user profile details.
 */
@Data
public class UpdateProfileRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String profilePhoto;
    private LocalDate dateOfBirth;
    private String state;
    private String city;
    private String pinCode;
    private String address;
    private String aadhaarNumber;
}
