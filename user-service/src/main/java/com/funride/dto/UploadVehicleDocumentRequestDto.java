package com.funride.dto;

import com.funride.enums.VehicleDocumentType;
import lombok.Data;

import java.util.UUID;

@Data
public class UploadVehicleDocumentRequestDto {

    private UUID vehicleId;

    private VehicleDocumentType documentType;

    private String document;
}