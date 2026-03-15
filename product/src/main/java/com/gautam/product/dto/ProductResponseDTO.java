package com.gautam.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private String category;
    private String brand;
    private Integer quantity;
    private String description;
    private BigDecimal price;



}
