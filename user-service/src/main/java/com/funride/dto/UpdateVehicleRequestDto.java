package com.funride.dto;

import lombok.Data;

/**
 * Request DTO for updating vehicle details
 * or vehicle documents.
 */
@Data
public class UpdateVehicleRequestDto {

    private String brand;
    private String model;
    private String color;
    private Integer seatCapacity;

    private String document;
}