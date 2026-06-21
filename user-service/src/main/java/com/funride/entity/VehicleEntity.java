package com.funride.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity to represent the vehicle table in DB.
 */
@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "vehicle_number", unique = true)
    private String vehicleNumber;

    private String brand;

    private String model;

    private String color;

    @Column(name = "seat_capacity")
    private Integer seatCapacity;

    @Column(name = "aadhaar_verified")
    private Boolean aadhaarVerified;

    @Column(name = "driving_license_verified")
    private Boolean drivingLicenseVerified;

    @Column(name = "rta_verified")
    private Boolean rtaVerified;

    @Column(name = "insurance_verified")
    private Boolean insuranceVerified;

    @Column(name = "aadhar_document" ,columnDefinition = "TEXT")
    private String aadharDocument;

    @Column(name = "driving_license_document" ,columnDefinition = "TEXT")
    private String drivingLicenseDocument;

    @Column(name = "insurance_document" ,columnDefinition = "TEXT")
    private String insuranceDocument;

    @Column(name = "rta_document",columnDefinition = "TEXT")
    private String rtaDocument;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
