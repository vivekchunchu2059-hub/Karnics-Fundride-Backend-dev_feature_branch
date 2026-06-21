package com.funride.constants;

import lombok.NoArgsConstructor;

/**
 * User-service constants used across the service.
 */
@NoArgsConstructor
public class AppConstant {

    public static final String PROFILE_CREATED_SUCCESSFULLY = "Profile created successfully";
    public static final String PROFILE_FETCHED_SUCCESSFULLY = "User profile fetched successfully";
    public static final String PROFILE_UPDATED_SUCCESSFULLY = "Profile updated successfully";

    public static final String STATES_FETCHED_SUCCESSFULLY = "States fetched successfully";
    public static final String CITIES_FETCHED_SUCCESSFULLY = "Cities fetched successfully";
    public static final String VEHICLE_NUMBER_REGEX = "^[A-Z]{2}\\s?[0-9]{1,2}\\s?[A-Z]{1,3}\\s?[0-9]{4}$";
    public static final String KYC_STATUS_FETCHED_SUCCESSFULLY = "KYC details fetched successfully";
    public static final String AADHAAR_REGEX = "^[2-9]{1}[0-9]{11}$";
}
