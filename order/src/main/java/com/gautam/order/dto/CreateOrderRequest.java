package com.gautam.order.dto;

import com.gautam.order.model.OrderItems;
import com.gautam.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long customerId;
    private List<OrderitemRequest> items;
    private AddressRequest shippingAddress;

}
