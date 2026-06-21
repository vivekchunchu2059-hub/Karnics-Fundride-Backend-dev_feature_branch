package com.funride.repository;

import com.funride.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing refresh token records.
 */
@Transactional
public interface RefreshTokenRepository
        extends JpaRepository<RefreshTokenEntity, UUID> {

    /**
     * Finds refresh token record by token value.
     */
    Optional<RefreshTokenEntity> findByToken(String token);

    /**
     * Deletes all refresh tokens associated with a phone number.
     *
     * @param phone user phone number
     */
    void deleteByPhone(String phone);
}
