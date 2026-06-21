package com.funride.service;

import com.funride.response.ApiResponse;

/**
 * Service contract for fetching Indian states and cities from an external
 * location API.
 */
public interface AddressService {

    /**
     * Fetches all states for India.
     *
     * @return standardized API response with states data
     */
    ApiResponse<Object> getStates();

    /**
     * Fetches cities for a given Indian state.
     *
     * @param state state name (for example, "Maharashtra")
     * @return standardized API response with cities data
     */
    ApiResponse<Object> getCities(String state);
}
