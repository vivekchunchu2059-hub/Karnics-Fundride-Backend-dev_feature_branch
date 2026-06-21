package com.funride.repository;

import com.funride.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing vehicle data.
 */
public interface VehicleRepository
        extends JpaRepository<VehicleEntity, UUID> {

    List<VehicleEntity> findAllByUserIdAndIsDeletedFalse(UUID userId);
    Optional<VehicleEntity> findByIdAndUserIdAndIsDeletedFalse(UUID vehicleId, UUID userId);
    boolean existsByVehicleNumber(String vehicleNumber);
    Optional<VehicleEntity> findByIdAndIsDeletedFalse(UUID vehicleId);
}
