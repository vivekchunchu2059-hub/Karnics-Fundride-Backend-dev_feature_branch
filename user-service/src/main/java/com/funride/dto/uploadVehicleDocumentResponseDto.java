package com.funride.dto;

import com.funride.enums.VehicleDocumentType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class uploadVehicleDocumentResponseDto {

    private UUID vehicleId;

    private VehicleDocumentType documentType;
}