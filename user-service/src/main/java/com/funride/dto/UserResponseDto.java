package com.funride.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO to return user profile details to the ride api.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
        private UUID id;
        private String phoneNumber;
        private String firstName;
        private String lastName;
        private String profilePhoto;
        private Integer trustScore;
        private Boolean profileCompleted;

}

