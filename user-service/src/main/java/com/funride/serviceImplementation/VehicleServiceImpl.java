package com.funride.ServiceImplementation.VehicleServiceImpl;

import com.funride.dto.*;
import com.funride.entity.UserEntity;
import com.funride.entity.VehicleEntity;
import com.funride.enums.VehicleDocumentType;
import com.funride.exception.BadRequestException;
import com.funride.exception.ResourceNotFoundException;
import com.funride.repository.UserRepository;
import com.funride.repository.VehicleRepository;
import com.funride.response.ApiResponse;
import com.funride.service.RideClient;
import com.funride.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.funride.constants.AppConstant.VEHICLE_NUMBER_REGEX;

/**
 * Service implementation responsible for vehicle management operations.
 *
 * <p>
 * Handles vehicle creation, update, retrieval and validation.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final MessageSource messageSource;
    private final RideClient rideClient;

    /**
     * Fetches vehicles details.
     *
     * @return API response containing vehicle data
     */

    @Override
    public ApiResponse<?> getVehicles(String phone, UUID vehicleId, VehicleDocumentType documentType) {
        log.info("Fetching vehicles for phone={}", phone);

        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() -> {
                    log.warn("User not found for phone={}", phone);

                    return new ResourceNotFoundException(
                            messageSource.getMessage(
                                    "error.user.not.found",
                                    null,
                                    Locale.getDefault()
                            )
                    );
                });

        /*
         * Document Request
         */
        if (vehicleId != null && documentType != null) {

            VehicleEntity vehicle = vehicleRepository.findByIdAndUserIdAndIsDeletedFalse(vehicleId, user.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            messageSource.getMessage(
                                                    "error.vehicle.not.found",
                                                    null,
                                                    Locale.getDefault()
                                            )
                                    ));

            String document = getDocument(vehicle, documentType);
            return new ApiResponse<>(
                    true,
                    "Document fetched successfully",
                    VehicleDocumentDto.builder()
                            .documentType(documentType.name())
                            .document(document)
                            .build()
            );
        }

        /*
         * Vehicle List Request
         */
        List<VehicleEntity> vehicles = vehicleRepository.findAllByUserIdAndIsDeletedFalse(user.getId());
        List<VehicleResponseDto> response = vehicles.stream()
                .map(this::mapToVehicleResponseDto)
                .toList();


        log.info("Vehicle document fetched successfully");
        return new ApiResponse<>(
                true,
                "Vehicles fetched successfully",
                response
        );
    }

    /**
     * Uploads a vehicle document.
     *
     * <p>
     * Creates a draft vehicle when uploading the first document.
     * Otherwise, updates the document for an existing vehicle.
     * </p>
     *
     * @param requestDto document upload request
     * @return uploaded document details
     */
    @Override
    public ApiResponse<?> uploadVehicleDocument(String phone, UploadVehicleDocumentRequestDto requestDto) {
        log.info("Upload vehicle document API called");

        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "error.user.not.found",
                                        null,
                                        Locale.getDefault()
                                )
                        ));

        VehicleEntity vehicle;

        /*
         * First document upload
         */
        if (requestDto.getVehicleId() == null) {
            log.info("Creating draft vehicle");

            vehicle = VehicleEntity.builder()
                    .userId(user.getId())
                    .isDeleted(false)
                    .aadhaarVerified(false)
                    .drivingLicenseVerified(false)
                    .insuranceVerified(false)
                    .rtaVerified(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            vehicle = vehicleRepository.save(vehicle);

            log.info("Draft vehicle created. VehicleId={}", vehicle.getId());

        } else {

            vehicle = vehicleRepository.findByIdAndUserIdAndIsDeletedFalse(requestDto.getVehicleId(), user.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    messageSource.getMessage(
                                            "error.vehicle.not.found",
                                            null,
                                            Locale.getDefault()
                                    )
                            ));
        }

        updateDocument(vehicle, requestDto.getDocumentType(), requestDto.getDocument());
        vehicle.setUpdatedAt(LocalDateTime.now());

        vehicleRepository.save(vehicle);

        return new ApiResponse<>(
                true,
                "Document uploaded successfully",
                uploadVehicleDocumentResponseDto.builder()
                        .vehicleId(vehicle.getId())
                        .documentType(requestDto.getDocumentType())
                        .build()
        );
    }

    /**
     * Completes vehicle creation by saving vehicle details.
     *
     * @param requestDto vehicle information
     * @return created vehicle details
     */
    @Override
    public ApiResponse<VehicleResponseDto> createVehicle(String phone, CreateVehicleRequestDto requestDto) {

        log.info("Saving vehicle details");
        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "error.user.not.found",
                                        null,
                                        Locale.getDefault()
                                )
                        ));

        VehicleEntity vehicle = vehicleRepository.findByIdAndUserIdAndIsDeletedFalse(requestDto.getVehicleId(), user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        messageSource.getMessage(
                                                "error.vehicle.not.found",
                                                null,
                                                Locale.getDefault()
                                        )
                                ));

        String vehicleNumber = requestDto.getVehicleNumber().trim().toUpperCase();

        if (!vehicleNumber.matches(VEHICLE_NUMBER_REGEX)) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.invalid.vehicle.number.format",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        /*
         * Check duplicate number
         * only if vehicle number changed
         */
        if (!vehicleNumber.equalsIgnoreCase(vehicle.getVehicleNumber())) {

            if (vehicleRepository.existsByVehicleNumber(vehicleNumber)) {
                throw new BadRequestException(
                        messageSource.getMessage(
                                "error.vehicle.already.exists",
                                null,
                                Locale.getDefault()
                        )
                );
            }
        }

        vehicle.setVehicleNumber(vehicleNumber);
        vehicle.setBrand(requestDto.getBrand());
        vehicle.setModel(requestDto.getModel());
        vehicle.setColor(requestDto.getColor());
        vehicle.setSeatCapacity(requestDto.getSeatCapacity());
        vehicle.setUpdatedAt(LocalDateTime.now());

        vehicle = vehicleRepository.save(vehicle);

        log.info("Vehicle details saved successfully");

        return new ApiResponse<>(
                true,
                "Vehicle created successfully",
                mapToVehicleResponseDto(vehicle)
        );
    }

    /**
     * Updates an existing vehicle belonging to the authenticated user.
     *
     * @param phone authenticated user's phone number
     * @param vehicleId vehicle id to update
     * @param requestDto updated vehicle details
     * @return API response containing updated vehicle details
     */
    @Override
    public ApiResponse<?> updateVehicle(String phone, UUID vehicleId, VehicleDocumentType documentType, UpdateVehicleRequestDto requestDto) {

        log.info("Updating vehicle. Phone={}, VehicleId={}, DocumentType={}", phone, vehicleId, documentType);
        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "error.user.not.found",
                                        null,
                                        Locale.getDefault()
                                )
                        ));

        VehicleEntity vehicle = vehicleRepository.findByIdAndUserIdAndIsDeletedFalse(vehicleId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "error.vehicle.not.found",
                                        null,
                                        Locale.getDefault()
                                )
                        ));

        /*
         * Document Update Request
         */
        if (documentType != null) {
            updateDocument(vehicle, documentType, requestDto.getDocument());
            vehicle.setUpdatedAt(LocalDateTime.now());
            vehicleRepository.save(vehicle);

            return new ApiResponse<>(
                    true,
                    "Document updated successfully",
                    VehicleDocumentDto.builder()
                            .documentType(documentType.name())
                            .document(requestDto.getDocument())
                            .build()
            );
        }

        /*
         * Vehicle Details Update Request
         */
        if (requestDto.getBrand() != null) {vehicle.setBrand(requestDto.getBrand());}
        if (requestDto.getModel() != null) {vehicle.setModel(requestDto.getModel());}
        if (requestDto.getColor() != null) {vehicle.setColor(requestDto.getColor());}
        if (requestDto.getSeatCapacity() != null) {vehicle.setSeatCapacity(requestDto.getSeatCapacity());}

        vehicle.setUpdatedAt(LocalDateTime.now());
        vehicle = vehicleRepository.save(vehicle);

        log.info("Vehicle updated successfully. VehicleId={}", vehicle.getId());

        return new ApiResponse<>(
                true,
                "Vehicle updated successfully",
                mapToVehicleResponseDto(vehicle)
        );

    }


    /**
     * Soft deletes a vehicle belonging to the user.
     *
     * <p>
     * The vehicle cannot be deleted if it is associated with any
     * active rides having SCHEDULED or IN_PROGRESS status.
     * </p>
     *
     * @param phone authenticated user's phone number
     * @param vehicleId vehicle id to delete
     * @return success response after deleting the vehicle
     */
    @Override
    public ApiResponse<String> deleteVehicle(String phone, UUID vehicleId) {

        log.info("Deleting vehicle. Phone={}, VehicleId={}", phone, vehicleId);

        UserEntity user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "error.user.not.found",
                                        null,
                                        Locale.getDefault()
                                )
                        ));

        log.info("Authenticated userId={}", user.getId());

        VehicleEntity vehicle = vehicleRepository.findByIdAndUserIdAndIsDeletedFalse(vehicleId, user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        messageSource.getMessage(
                                                "error.vehicle.not.found",
                                                null,
                                                Locale.getDefault()
                                        )
                                ));

        log.info("Vehicle present={}", vehicle);

        log.info("Checking active rides for vehicleId={}", vehicleId);

        Boolean hasActiveRide;

        try {
            hasActiveRide = rideClient.hasActiveRideByVehicle(vehicleId);

        } catch (Exception ex) {
            log.error("Failed to validate active rides for vehicleId={}", vehicleId, ex);

            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.unable.get.status",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        if (Boolean.TRUE.equals(hasActiveRide)) {

            log.warn("Vehicle {} has active rides", vehicleId);

            throw new BadRequestException(
                    messageSource.getMessage(
                            "error.vehicle.has.active.ride",
                            null,
                            Locale.getDefault()
                    )
            );
        }
        vehicle.setIsDeleted(true);
        vehicle.setDeletedAt(LocalDateTime.now());
        vehicle.setUpdatedAt(LocalDateTime.now());

        vehicleRepository.save(vehicle);

        log.info("Vehicle soft deleted successfully. VehicleId={}", vehicleId);

        return new ApiResponse<>(
                true,
                "Vehicle deleted successfully",
                null
        );
    }


    /**
     * Fetches vehicle details by vehicle id.
     *
     * Used by Ride Service to retrieve vehicle information
     * while preparing ride responses.
     */
    @Override
    public VehicleRideDetailDto getVehicleById(UUID vehicleId) {

        log.info("Fetching vehicle details for vehicleId={}", vehicleId);
        VehicleEntity vehicle =
                vehicleRepository.findByIdAndIsDeletedFalse(vehicleId)
                        .orElseThrow(() ->{
                            return new ResourceNotFoundException(
                                messageSource.getMessage(
                                    "error.vehicle.not.found",
                                    null,
                                    Locale.getDefault()
                                )
                            );
                        });

        log.debug("Vehicle details fetched successfully for vehicleId={}", vehicleId);
        return new VehicleRideDetailDto(
                vehicle.getId(),
                vehicle.getVehicleNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getSeatCapacity()
,                vehicle.getAadhaarVerified(),
                vehicle.getDrivingLicenseVerified()
        );
    }



    /**
     * Converts a VehicleEntity into VehicleResponseDto.
     *
     * This method is used to map vehicle database entity data
     * into a response DTO before sending it to the client.
     */
    private VehicleResponseDto mapToVehicleResponseDto(
            VehicleEntity vehicle) {

        return VehicleResponseDto.builder()
                .id(vehicle.getId())
                .userId(vehicle.getUserId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .color(vehicle.getColor())
                .seatCapacity(vehicle.getSeatCapacity())
                .rtaVerified(vehicle.getRtaVerified())
                .insuranceVerified(vehicle.getInsuranceVerified())
                .aadhaarVerified(vehicle.getAadhaarVerified())
                .drivingLicenseVerified(vehicle.getDrivingLicenseVerified())
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }

    /**
     * Retrieves the requested document from a vehicle.
     *
     * @param vehicle vehicle entity
     * @param documentType document type to fetch
     * @return document content
     */
    private String getDocument(VehicleEntity vehicle, VehicleDocumentType documentType) {

        log.debug("Fetching {} document for vehicleId={}", documentType, vehicle.getId());
        return switch (documentType) {
            case AADHAAR -> vehicle.getAadharDocument();
            case LICENSE -> vehicle.getDrivingLicenseDocument();
            case INSURANCE -> vehicle.getInsuranceDocument();
            case RTA -> vehicle.getRtaDocument();
        };
    }

    /**
     * Updates the requested vehicle document.
     *
     * @param vehicle vehicle entity
     * @param documentType document type to update
     * @param document base64 encoded document content
     */
    private void updateDocument(VehicleEntity vehicle, VehicleDocumentType documentType, String document) {
        log.info("Updating {} document for vehicleId={}", documentType, vehicle.getId());
        switch (documentType) {
            case AADHAAR -> vehicle.setAadharDocument(document);
            case LICENSE -> vehicle.setDrivingLicenseDocument(document);
            case INSURANCE -> vehicle.setInsuranceDocument(document);
            case RTA -> vehicle.setRtaDocument(document);
        }
        log.debug("{} document updated successfully for vehicleId={}", documentType, vehicle.getId());
    }
}


