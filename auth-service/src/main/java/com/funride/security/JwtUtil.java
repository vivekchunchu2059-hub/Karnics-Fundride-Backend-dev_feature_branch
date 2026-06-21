package com.funride.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility for generating JWT tokens after OTP verification.
 */

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration-ms}")
    private long accessExpirationMs;

    @Value("${jwt.refresh.expiration-ms}")
    private long refreshExpirationMs;

    /**
     * Generates a JWT for the given phone number.
     *
     * @param phone user's phone number (token subject)
     * @return signed JWT string
     */
    public String generateAccessToken(String phone) {

        log.debug("Generating access token for phone={}", phone);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessExpirationMs);
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        String token =  Jwts.builder()
                .setSubject(phone)
                .claim("type", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.debug("Access token generated successfully for phone={}", phone);
        return token;
    }

    /**
     * Generates a JWT refresh token for the specified phone number.
     *
     * @param phone user's phone number
     * @return signed refresh token
     */
    public String generateRefreshToken(String phone) {

        log.debug("Generating refresh token for phone={}", phone);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationMs);
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        String token =  Jwts.builder()
                .setSubject(phone)
                .claim("type", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.debug("Refresh token generated successfully for phone={}", phone);

        return token;
    }

    /**
     * Extracts phone number from JWT token.
     *
     * @param token JWT token
     * @return phone number stored in token subject
     */
    public String extractPhone(String token) {

        log.info("Extracting phone number from JWT token");
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        String phone = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        log.info("Phone number extracted successfully from JWT token");

        return phone;
    }

    /**
     * Validates JWT token signature and expiration.
     *
     * @param token JWT token
     * @return true if token is valid, otherwise false
     */
    public boolean validateToken(String token) {
        try {
            log.info("Validating JWT token");
            Key key = Keys.hmacShaKeyFor(secret.getBytes());

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            log.info("JWT token validation successful");
            return true;

        } catch (Exception ex) {

            log.warn("JWT token validation failed: {}", ex.getClass().getSimpleName());
            return false;
        }
    }
}
