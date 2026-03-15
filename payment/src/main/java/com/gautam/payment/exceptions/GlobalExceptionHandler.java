package com.gautam.payment.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.gautam.payment.dto.ErrorResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException resourceNotFoundException,
            HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                resourceNotFoundException.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
