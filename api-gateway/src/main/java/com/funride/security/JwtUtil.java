package com.funride.security;


import lombok.extern.slf4j.Slf4j;

import com.funride.exception.ExpiredTokenException;
import com.funride.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;


/**
 * Utility class for JWT validation
 * and claim extraction.
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Creates JWT signing key from configured secret.
     *
     * @return signing key
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    /**
     * Validates JWT token.
     *
     * @param token JWT token
     * @throws ExpiredTokenException if token is expired
     * @throws InvalidTokenException if token is invalid
     */
    public void validateToken(String token) {

        try {
            log.debug("Validating JWT token");
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            log.debug("JWT token validated successfully");

        } catch (ExpiredJwtException ex) {
            log.warn("JWT token has expired");
            throw new ExpiredTokenException(
                    "Token has expired"
            );

        } catch (JwtException | IllegalArgumentException ex) {

            log.warn("Invalid JWT token received", ex);
            throw new InvalidTokenException(
                    "Invalid token"
            );
        }
    }

    /**
     * Extracts phone number from JWT subject.
     *
     * @param token JWT token
     * @return authenticated user's phone number
     */
    public String extractPhone(String token) {

        log.info("Extracting phone number from JWT token");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("Phone number extracted successfully");
        return claims.getSubject();
    }
}