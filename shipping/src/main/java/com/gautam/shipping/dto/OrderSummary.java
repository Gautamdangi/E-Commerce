package com.gautam.shipping.dto;

import lombok.Data;

@Data
public class OrderSummary {
    private Long orderId;
    private String customerId;
    private String status;
}
