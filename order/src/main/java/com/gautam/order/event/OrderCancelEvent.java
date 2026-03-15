package com.gautam.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelEvent {
    private Long orderId;
    private Long quantity;

    public OrderCancelEvent(Long orderId, List<OrderItemEvent> items) {
    }
}
