package com.gio.couponsystem.exception;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerExceptionResponse> serverException(Exception e) {
        ServerExceptionResponse response = ServerExceptionResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();

        log.error("Exception occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ServerExceptionResponse> customException(CustomException e) {
        ServerExceptionResponse response = ServerExceptionResponse.builder()
                .status(e.getExceptionCode().getStatus().value())
                .error(e.getExceptionCode().getStatus().getReasonPhrase())
                .message(e.getExceptionCode().getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        log.error("Exception occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> validationException(
            MethodArgumentNotValidException e) {
        Map<String, String> validationErrors = getValidationErrors(e);

        ValidationExceptionResponse response = ValidationExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Invalid arguments provided")
                .timestamp(System.currentTimeMillis())
                .validationErrors(validationErrors)
                .build();

        log.error("Validation Exception: {}", validationErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> getValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return validationErrors;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public void noResourceFoundException(NoResourceFoundException e) {
        log.warn("Resource not found: {}", e.getMessage());
    }

}

