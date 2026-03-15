package com.gautam.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductRequest {

    private String productName;
    private String description;
    private String category;
    private String brand;
    private Integer quantity;
    private BigDecimal price;
    private Boolean isActive;
}
