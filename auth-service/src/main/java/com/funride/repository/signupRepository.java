package com.funride.repository;

import com.funride.entity.signupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for performing database operations
 * on signup entity.
 */
public interface signupRepository extends JpaRepository<signupEntity, UUID> {
    /**
     * Finds user by phone number.
     *
     * @param phone user's phone number
     * @return optional signup entity
     */
    Optional<signupEntity> findByPhone(String phone);
}