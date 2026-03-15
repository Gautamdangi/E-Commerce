package com.gautam.order.dao;

import com.gautam.order.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItem extends JpaRepository<OrderItems,Long> {
}
