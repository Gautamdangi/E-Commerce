package com.gautam.order.exceptions;

public class InsufficientStockResource extends RuntimeException {
    public InsufficientStockResource(String message) {
        super(message);
    }
}
