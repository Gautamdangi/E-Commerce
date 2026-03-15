package com.gautam.shipping.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name =" shipments")
@Entity
public class ShippingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false,unique = true)//for idempotency
    private Long orderId;

    @Column(name = "tracking_number")
    private String trackingNumber;



    @Enumerated(EnumType.STRING)
    @Column(name = "Shipped status")
    private ShippingStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//schedule date time

}
