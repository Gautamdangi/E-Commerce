package com.gautam.shipping.feign;

import com.gautam.shipping.dto.OrderSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ORDER-SERVICE")
public interface OrderClient {

    @GetMapping("/api/orders/{orderId}/response")
    OrderSummary getOrder(@PathVariable  Long orderId);
}
