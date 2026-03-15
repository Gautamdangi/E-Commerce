package com.gautam.payment.feign;
import com.gautam.payment.dto.OrderSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient("ORDER-SERVICE")
public interface OrderClient {

    @GetMapping("/api/orders/{orderId/response")
    OrderSummary getOrder(@PathVariable Long orderId);

    //post request to update request in order
    @PostMapping
    void markOrderPaid(@PathVariable Long orderId);
}
