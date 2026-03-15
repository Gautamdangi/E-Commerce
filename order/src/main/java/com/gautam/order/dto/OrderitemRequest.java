package com.gautam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderitemRequest {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
