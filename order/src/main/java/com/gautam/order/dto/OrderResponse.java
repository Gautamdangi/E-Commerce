package com.gautam.order.dto;

import com.gautam.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long customerId;
    private  String orderCode;
    private BigDecimal totalAmount;
    private OrderStatus status;

    //private List<OrderItemResponse> Items;

}
