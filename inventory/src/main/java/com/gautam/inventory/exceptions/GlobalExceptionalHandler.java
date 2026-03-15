package com.gautam.inventory.exceptions;


import com.gautam.inventory.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionalHandler {


    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientException(
            InsufficientStockException insufficientStockException,
            HttpServletRequest request
    ) {
        log.error("Stock is not available");
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                insufficientStockException.getMessage(),
                request.getRequestURI()


        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handResourceException(
            ResourceNotFoundException resourceNotFoundException,
            HttpServletRequest request
    ) {
        log.error("Resource not found");
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NO_CONTENT.value(),
                resourceNotFoundException.getMessage(),
                request.getRequestURI()


        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);

    }
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(
            FeignException feignException,
            HttpServletRequest request
    ){
        log.error("Feign client is unavailable to processed request");
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                feignException.getMessage(),
                request.getRequestURI()
        );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(
            ServiceUnavailableException serviceUnavailableException,
            HttpServletRequest request

    ){
        log.error("Service is currently unavailable for for this request");
        ErrorResponse error = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                serviceUnavailableException.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);

    }
}
