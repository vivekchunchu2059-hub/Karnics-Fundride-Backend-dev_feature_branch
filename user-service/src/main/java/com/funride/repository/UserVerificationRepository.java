package com.funride.repository;

import com.funride.entity.UserVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for performing operations on user verification.
 */
public interface UserVerificationRepository
        extends JpaRepository<UserVerificationEntity, UUID> {

    Optional<UserVerificationEntity> findByUserId(UUID userId);
}