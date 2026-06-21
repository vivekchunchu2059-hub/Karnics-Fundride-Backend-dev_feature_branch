package com.funride.controller;

import com.funride.response.ApiResponse;
import com.funride.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for address lookup: Indian states and cities.
 */
@Slf4j
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * Returns all states in India.
     *
     * @return standardized API response with states data
     */
    @GetMapping("/states")
    public ResponseEntity<ApiResponse<Object>> getStates() {

        log.info("Get states API called");
        ApiResponse<Object> response = addressService.getStates();
        log.info("States fetched successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Returns cities for the given state.
     *
     * @param state state name (query parameter)
     * @return standardized API response with cities data
     */
    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<Object>> getCities(
            @RequestParam String state
    ) {

        log.info("Get cities API called for state={}", state);
        ApiResponse<Object> response = addressService.getCities(state);
        log.info("Cities fetched successfully for state={}", state);
        return ResponseEntity.ok(response);
    }
}
