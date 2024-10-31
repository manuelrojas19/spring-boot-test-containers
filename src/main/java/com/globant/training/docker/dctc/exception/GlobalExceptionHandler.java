package com.globant.training.docker.dctc.exception;

import com.globant.training.docker.dctc.dto.AlertResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle a specific exception, like `IllegalArgumentException`
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AlertResponse> handleResourceNotFoundException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(AlertResponse.builder()
                .alertStatus("Alert was not sent, user was not found")
                .build(), HttpStatus.NOT_FOUND);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
