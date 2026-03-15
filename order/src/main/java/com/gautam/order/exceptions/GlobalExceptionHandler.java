package com.gautam.order.exceptions;

import com.gautam.order.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientStockResource.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(
            InsufficientStockResource stockResource,
            HttpServletRequest request
    ){
        log.error("Stock is unavailable for this product");
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                stockResource.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceException(
            ResourceNotFoundException resource,
            HttpServletRequest request
    ){
        log.error("Resource is currently not available");
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                resource.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException validationException,
            HttpServletRequest request
    ){
        log.error("This not a valid product");
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                validationException.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
