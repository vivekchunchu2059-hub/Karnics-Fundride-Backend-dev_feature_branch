package com.funride.dto;

import lombok.Data;

import java.util.UUID;

/**
 * DTO used for creating user vehicle.
 */
@Data
public class CreateVehicleRequestDto {

    private UUID vehicleId;
    private String vehicleNumber;
    private String brand;
    private String model;
    private String color;
    private Integer seatCapacity;
}
