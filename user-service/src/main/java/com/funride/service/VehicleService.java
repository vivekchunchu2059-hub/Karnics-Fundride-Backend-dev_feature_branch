package com.funride.service;

import com.funride.dto.*;
import com.funride.enums.VehicleDocumentType;
import com.funride.response.ApiResponse;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing vehicles operations.
 */
public interface VehicleService {

    ApiResponse<VehicleResponseDto> createVehicle(String phone, CreateVehicleRequestDto requestDto);
    ApiResponse<?> updateVehicle(String phone, UUID vehicleId, VehicleDocumentType documentType, UpdateVehicleRequestDto requestDto);    VehicleRideDetailDto getVehicleById(UUID vehicleId);
    ApiResponse<?> getVehicles(String phone, UUID vehicleId, VehicleDocumentType documentType);
    ApiResponse<?> uploadVehicleDocument(String phone, UploadVehicleDocumentRequestDto requestDto);
    ApiResponse<String> deleteVehicle(String phone, UUID vehicleId);
}
