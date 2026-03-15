package com.gautam.order.dto;

import lombok.Data;

@Data
public class InventoryResponse {
    private Long productId;
    private Integer availableQuantity;
    private Integer reserveQuantity;
}
