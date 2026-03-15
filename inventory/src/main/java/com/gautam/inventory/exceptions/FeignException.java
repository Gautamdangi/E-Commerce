package com.gautam.inventory.exceptions;

public class FeignException extends RuntimeException {
    public FeignException(String message) {
        super(message);
    }
}
