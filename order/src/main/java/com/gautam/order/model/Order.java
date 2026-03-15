package com.gautam.order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name ="order_entity")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "customer_id")
    private Long customerId;// get from user and set accordingly after user service

    @Column(name = "order_code",nullable = false)
    private  String orderCode;   // e.g. "ORD-20260218-0001"

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "update_date")
    private LocalDateTime updatedAt;

    @Column(name = "address")
    @Embedded
    private Address shippingAddress;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL, orphanRemoval = true)//orphan removal is used to hibernate will auto delete the child entity from db if it is deleted from parent
    private List<OrderItems> items = new ArrayList<>();

    public void addItem(OrderItems item) {

        items.add(item);
        item.setOrder(this);

    }

}
