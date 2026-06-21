package com.funride.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO used for vehicle entity for ride service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRideDetailDto {

    private UUID id;
    private String vehicleNumber;
    private String brand;
    private String model;
    private String color;
    private Integer seatCapacity;
    private Boolean aadhaarVerified;
    private Boolean drivingLicenseVerified;
}
