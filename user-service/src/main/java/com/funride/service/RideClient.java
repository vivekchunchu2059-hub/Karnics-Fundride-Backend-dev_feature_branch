package com.funride.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Feign client for communicating with Ride Service.
 */
@FeignClient(name = "RIDE-BOOKING-SERVICE")
public interface RideClient {

    @GetMapping("/ride/{vehicleId}/active")
    Boolean hasActiveRideByVehicle(
            @PathVariable("vehicleId") UUID vehicleId
    );
}