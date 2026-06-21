package com.funride.serviceImplementation;

import com.funride.response.ApiResponse;
import com.funride.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.funride.constants.AppConstant.CITIES_FETCHED_SUCCESSFULLY;
import static com.funride.constants.AppConstant.STATES_FETCHED_SUCCESSFULLY;

/**
 * Service implementation for address lookup operations.
 * Calls the Countries Now API to fetch Indian states and cities,
 * then wraps results in {@link ApiResponse}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final RestTemplate restTemplate;

    /** Base URL for the Countries Now countries API. */
    private static final String BASE_URL =
            "https://countriesnow.space/api/v0.1/countries";

    /**
     * Fetches all states in India from the external API and returns a standardized response.
     *
     * @return API response with success flag, message, and states data from Countries Now
     */
    @Override
    public ApiResponse<Object> getStates() {

        log.info("Fetching states from Countries Now API");

        String url = BASE_URL + "/states";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = new HashMap<>();
        request.put("country", "India");

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );

        log.info("States fetched successfully");

        return new ApiResponse<>(
                true,
                STATES_FETCHED_SUCCESSFULLY,
                response.getBody()
        );
    }

    /**
     * Fetches cities for the given Indian state from the external API and returns a standardized response.
     *
     * @param state state name to look up (for example, "Maharashtra")
     * @return API response with success flag, message, and cities data from Countries Now
     */
    @Override
    public ApiResponse<Object> getCities(String state) {

        log.info("Fetching cities for state={}", state);

        String url = BASE_URL + "/state/cities";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = new HashMap<>();
        request.put("country", "India");
        request.put("state", state);

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );

        log.info("Cities fetched successfully");

        return new ApiResponse<>(
                true,
                CITIES_FETCHED_SUCCESSFULLY,
                response.getBody()
        );
    }
}
