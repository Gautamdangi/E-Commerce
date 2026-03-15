package com.gautam.product.exceptions;


import com.gautam.product.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionalHandler {


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalException(
            IllegalStateException illegalStateException,
            HttpServletRequest request
    ) {
        log.error("Not allowed to make changes");
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                illegalStateException.getMessage(),
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
}
