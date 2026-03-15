package com.gautam.inventory.dto;

import lombok.Data;

@Data
public class AddStockRequest {
    private Long productId;
    private Integer quantity;
}
