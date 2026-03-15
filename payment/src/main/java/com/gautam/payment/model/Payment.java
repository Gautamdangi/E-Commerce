package com.gautam.payment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name ="payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "order_id",nullable = false)
    private Long orderId;//comes from order service

    //payment reference no.
    @Column(name = "payment_reference")
    private String paymentReference;//(PAy-time)


    @Column(name="amount",nullable = false)
    private BigDecimal totalAmount;

    @Column(name= "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name= "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name="failure_reason")
    private String failureReason;

    private BigDecimal refundAmount;

    @Column(name = "transaction_id")
    private String transactionId; //it is mock from payment providers upi apps or cards

    @Column(name= "created_at",updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

//    @PrePersist
//    public void prePersist(){
//        createdAt = LocalDateTime.now();
//        status = null ? "PENDING": status;
//    }
//    @PreUpdate
//    public void preUpdate(){
//        updatedAt = LocalDateTime.now();
//    }
}

