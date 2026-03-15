package com.gautam.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSummary {
    private  Long orderId;
    private  Long customerId;
    private String orderCode;
    private BigDecimal totalAmount;
    private String status;



}
