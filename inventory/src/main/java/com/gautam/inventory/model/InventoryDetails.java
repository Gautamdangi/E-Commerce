package com.gautam.inventory.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.logging.log4j.util.Lazy;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Inventory")
public class InventoryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //comes from product service
    @Column(unique = true,nullable = false)
    private Long productId;


    @Column(name = "product",nullable = false)
    private String productName;

    @Column(nullable = false,name = "available_quantity")
    private Integer availableQuantity;
    
    @Column(nullable = false,name = "reserve_quantity")
    private Integer reserveQuantity;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "is_active")
    private Boolean isActive;


    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    //we can also manage date and time to keep record of inventory

}
