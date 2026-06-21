package com.funride.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for getting the vehicle entity.
 */
@Data
@Builder
public class VehicleResponseDto {

    private UUID id;
    private UUID userId;

    private String vehicleNumber;
    private String brand;
    private String model;
    private String color;

    private Integer seatCapacity;

    private Boolean aadhaarVerified;
    private Boolean drivingLicenseVerified;
    private Boolean rtaVerified;
    private Boolean insuranceVerified;
    private String document;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
