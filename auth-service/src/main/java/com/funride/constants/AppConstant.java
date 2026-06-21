package com.funride.constants;

import lombok.NoArgsConstructor;


/**
 * Auth-service constants used across the service.
 */
@NoArgsConstructor
public class AppConstant {

    // SignupServiceImpl
    public static final String OTP_PREFIX = "otp: ";
    public static final String OTP_SENT_SUCCESSFULLY = "OTP sent successfully";
    public static final String OTP_VERIFIED = "OTP has been verified";

    // Flow types
    public static final String LOGIN = "LOGIN";
    public static final String SIGNUP = "SIGNUP";

}