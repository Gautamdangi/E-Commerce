package com.gautam.order.controller;

import com.gautam.order.dto.CreateOrderRequest;
import com.gautam.order.dto.OrderResponse;
import com.gautam.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order",description = "APIs for managing Orders")
public class OrderController {

    @Autowired
            private OrderService orderService;

    @Operation(summary = "API for create order")
    @PostMapping
    public ResponseEntity<OrderResponse > createOrder(@RequestBody CreateOrderRequest request){
        return ResponseEntity.status(201).body(orderService.createOrder(request));
    }

    @Operation(summary = "API for confirm payment for order")
    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<OrderResponse> confirmPayment(@PathVariable Long orderId,
                                                        @RequestParam Long paymentId) {
        return ResponseEntity.ok(orderService.markOrderPaid(orderId));
    }

    @Operation(summary = "API for cancel order ")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId,
                                            @RequestParam String reason) {
        orderService.cancelOrder(orderId, reason);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "API for get order request")
    @GetMapping("/{orderId}/response")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

    // get  order details
}

