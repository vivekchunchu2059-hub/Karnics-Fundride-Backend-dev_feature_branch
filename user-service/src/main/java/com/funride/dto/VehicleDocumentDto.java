package com.funride.dto;

import com.funride.enums.VehicleDocumentType;
import lombok.Builder;
import lombok.Data;

/**
 * DTO used for vehicle document responses.
 */

@Data
@Builder
public class VehicleDocumentDto {

    /**
     * Type of vehicle document.
     */
    private String documentType;

    /**
     * Base64 encoded document content(for now).
     */
    private String document;
}