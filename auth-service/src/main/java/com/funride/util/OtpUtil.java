package com.funride.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Utility class for generating OTP values.
 */
@Slf4j
@Component
public class OtpUtil {

    /**
     * Generates a random 6-digit OTP.
     *
     * @return generated OTP as string
     */
    public String generateOtp() {
        log.debug("Generating OTP");

        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);

        log.debug("OTP generated successfully");
        return String.valueOf(otp);
    }
}