package com.funride.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funride.response.ApiResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Utility class for writing standardized API responses.
 */
@Slf4j
@UtilityClass
public class ResponseUtil {

    /**
     * Writes error response to client.
     *
     * @param exchange current server exchange
     * @param message error message
     * @return response publisher
     */
    public Mono<Void> writeResponse(
            ServerWebExchange exchange,
            HttpStatus status,
            String message) {

        try {
            log.debug("Writing error response. Status={}, Message={}", status, message);
            ApiResponse<Void> response =
                    new ApiResponse<>(false, message);

            byte[] bytes =
                    new ObjectMapper()
                            .writeValueAsBytes(response);

            exchange.getResponse()
                    .setStatusCode(status);

            exchange.getResponse()
                    .getHeaders()
                    .setContentType(
                            MediaType.APPLICATION_JSON
                    );

            return exchange.getResponse()
                    .writeWith(
                            Mono.just(
                                    exchange.getResponse()
                                            .bufferFactory()
                                            .wrap(bytes)
                            )
                    );

        } catch (Exception ex) {
            log.error("Failed to write error response", ex);
            return exchange.getResponse().setComplete();
        }
    }
}