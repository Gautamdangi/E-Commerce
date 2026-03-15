package com.gautam.order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Entity
@Table(name = " order_items")
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id",nullable = false)
    private Long productId;         // reference from product service

    @Column(name = "poduct_name")
    private String productName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;       //fetch from inventory

    @Column(name = "price",nullable = false)
    private BigDecimal price;           // fetch from  product service

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
