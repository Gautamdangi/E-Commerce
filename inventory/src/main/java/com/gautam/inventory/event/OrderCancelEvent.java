package com.gautam.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelEvent {
    private Long orderId;
    private List<OrderItemEvent> itemEvents;


}
