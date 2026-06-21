package com.funride.exception;

import com.funride.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling user-service exceptions.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles bad request exceptions.
     *
     * @param ex bad request exception
     * @return API response with BAD_REQUEST status
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex) {
        log.error("Bad request exception: {}", ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles resource not found exceptions.
     *
     * @param ex resource not found exception
     * @return API response with NOT_FOUND status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles unauthorized exceptions.
     *
     * @param ex unauthorized exception
     * @return API response with UNAUTHORIZED status
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(
            UnauthorizedException ex) {
        log.error("Unauthorized exception: {}", ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles all unhandled exceptions.
     *
     * @param ex generic exception
     * @return API response with INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(false, "Something went wrong");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
