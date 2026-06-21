package com.funride.repository;


import com.funride.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing user profile data.
 * Provides methods for checking phone number and email uniqueness.
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByPhone(String phone);
    boolean existsByAadhaarNumber(String aadhaarNumber);
}
