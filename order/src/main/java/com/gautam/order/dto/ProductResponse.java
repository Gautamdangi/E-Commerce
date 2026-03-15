package com.gautam.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
public class ProductResponse {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private boolean isActive;


    public boolean getIsActive() {
        return false;
    }
}
