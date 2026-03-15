package com.gautam.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrderItemEvent {
    private Long productId;
    private Integer quantity;
}
