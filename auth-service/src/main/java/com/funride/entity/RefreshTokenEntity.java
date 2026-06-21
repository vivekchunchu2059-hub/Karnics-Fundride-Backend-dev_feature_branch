package com.funride.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity used to store refresh tokens issued to users.
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    /**
     * User phone number associated with token.
     */
    @Column(nullable = false)
    private String phone;

    /**
     * JWT refresh token.
     */
    @Column(nullable = false, length = 1000)
    private String token;

    /**
     * Token expiration timestamp.
     */
    @Column(nullable = false)
    private LocalDateTime expiryDate;
}
