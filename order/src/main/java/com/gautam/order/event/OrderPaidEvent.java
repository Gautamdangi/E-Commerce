package com.gautam.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidEvent {
    private Long orderId;
    private Long customerId;
    private BigDecimal totalAmount;


}
