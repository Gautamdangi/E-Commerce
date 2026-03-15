package com.gautam.payment.dto;

import com.gautam.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentResponse {
    private Long id;
    private String paymentReference;
    private BigDecimal amount;
    private PaymentStatus status;
}
