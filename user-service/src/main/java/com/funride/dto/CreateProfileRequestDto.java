package com.funride.dto;

import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO used for creating user profile.
 */
@Data
public class CreateProfileRequestDto {

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