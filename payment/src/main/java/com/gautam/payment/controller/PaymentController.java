package com.gautam.payment.controller;

import com.gautam.payment.dto.PaymentResponse;
import com.gautam.payment.model.PaymentMethod;
import com.gautam.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment",description = "Operation related to payments")
public class PaymentController {
    @Autowired
   private PaymentService paymentService;

    @Operation(summary = "initiate payment")//short summary nes=xt to endpoints also we can add api response
    @ApiResponse(responseCode = "201",description = "payment initiated")
    @ApiResponse(responseCode = "400",description = "invalid payload")
    @PostMapping("/orders/{orderId}/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(
            @PathVariable Long orderId,
            @RequestParam PaymentMethod paymentMethod){
        return ResponseEntity.status(201).body(paymentService.initiatePayment(orderId,paymentMethod));

    }
    @Operation(summary = "confirm payment")
    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<PaymentResponse> confirm(
            @PathVariable Long paymentId,
            @RequestParam Boolean success,
    @RequestParam(required= false) String transactionId){
        return ResponseEntity.ok(paymentService.confirmPayment(paymentId,success,transactionId));

    }
    @Operation(summary = "refund payment")
    @PostMapping("/{paymentId}/refund")
    public  ResponseEntity<PaymentResponse> refund(@PathVariable Long paymentId, @RequestParam BigDecimal amount){
        return ResponseEntity.ok(paymentService.refundPayment(paymentId,amount));
    }

    @Operation(summary = "get  payment details")
    @GetMapping("/{paymentId}")
    public  ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId){
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }
}
