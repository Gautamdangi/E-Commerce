package com.gautam.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCreatedEvent {
    private Long productId;
    private String name;
    private Integer quantity;
}
