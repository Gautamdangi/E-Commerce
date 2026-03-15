package com.gautam.shipping.dto;

import com.gautam.shipping.model.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ShippingResponse {
    private Long id;
    private Long orderId;
    private String trackingNumber;
    private LocalDateTime updatedAt;
    private ShippingStatus status;


}
