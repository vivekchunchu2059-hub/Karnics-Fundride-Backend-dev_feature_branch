package com.funride.controller;

import com.funride.dto.*;
import com.funride.enums.VehicleDocumentType;
import com.funride.response.ApiResponse;
import com.funride.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Vehicle Controller responsible for handling vehicles related APIs
 * such as creating vehicles, viewing vehicles, and updating vehicles.
 */

@Slf4j
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Retrieves vehicles associated with the authenticated user.
     *
     * <p>
     * If both vehicleId and documentType are provided,
     * the requested vehicle document is returned.
     * Otherwise, all vehicles belonging to the user are returned.
     * </p>
     *
     * @param phone authenticated user's phone number
     * @param vehicleId vehicle identifier
     * @param documentType document type to retrieve (optional)
     * @return vehicle list or vehicle document details
     */
    @GetMapping("/get-vehicle")
    public ResponseEntity<ApiResponse<?>> getVehicles(
            @RequestHeader("X-User-Phone") String phone,
            @RequestParam(required = false) UUID vehicleId,
            @RequestParam(required = false) VehicleDocumentType documentType) {

        log.info("Get vehicles API called");

        ApiResponse<?> response =
                vehicleService.getVehicles(phone, vehicleId, documentType);

        log.info("Vehicles fetched successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new vehicle for the logged-in user.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleResponseDto>> createVehicle(
            @RequestHeader("X-User-Phone") String phone,
            @RequestBody CreateVehicleRequestDto requestDto) {

        log.info("Create vehicle API called for phone: {}", phone);

        ApiResponse<VehicleResponseDto> response =
                vehicleService.createVehicle(phone, requestDto);

        log.info("Vehicle created successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-document")
    public ResponseEntity<ApiResponse<?>>
    uploadVehicleDocument(
            @RequestHeader("X-User-Phone") String phone,
            @RequestBody UploadVehicleDocumentRequestDto requestDto) {

        return ResponseEntity.ok(
                vehicleService.uploadVehicleDocument(phone, requestDto)
        );
    }

    /**
     * Updates vehicle details or uploads a vehicle document.
     *
     * <p>
     * If documentType is provided, the specified document
     * will be updated. Otherwise vehicle details are updated.
     * </p>
     *
     * @param phone authenticated user's phone number
     * @param vehicleId vehicle identifier
     * @param documentType document type to update (optional)
     * @param requestDto vehicle update request
     * @return updated vehicle or document information
     */
    @PutMapping("/update-vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<?>> updateVehicle(
            @RequestHeader("X-User-Phone") String phone,
            @PathVariable UUID vehicleId,
            @RequestParam(required = false) VehicleDocumentType documentType,
            @RequestBody UpdateVehicleRequestDto requestDto) {

        log.info("Update vehicle API called. Phone={}, VehicleId={}, DocumentType={}",
                phone, vehicleId, documentType);

        ApiResponse<?> response = vehicleService.updateVehicle(
                        phone, vehicleId,
                        documentType, requestDto
                );

        log.info("Vehicle updated successfully");
        return ResponseEntity.ok(response);
    }


    /**
     * Soft deletes a vehicle belonging to the user.
     *
     * @param phone user's phone number
     * @param vehicleId vehicle id
     * @return success response
     */
    @DeleteMapping("/{vehicleId}/delete")
    public ResponseEntity<ApiResponse<String>> deleteVehicle(
            @RequestHeader("X-User-Phone") String phone,
            @PathVariable UUID vehicleId) {

        log.info("Delete vehicle API called. VehicleId={}", vehicleId);

        ApiResponse<String> response =
                vehicleService.deleteVehicle(phone, vehicleId);

        log.info("Vehicle deleted successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Fetches vehicle details by vehicle id.
     * @return vehicle information used by ride service
     */
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleRideDetailDto>
    getVehicleById(
            @PathVariable UUID vehicleId) {

        log.info("Get vehicle by id API called");
        VehicleRideDetailDto response =
                vehicleService.getVehicleById(vehicleId);

        log.info("Vehicle details fetched successfully");
        return ResponseEntity.ok(response);
    }


}
