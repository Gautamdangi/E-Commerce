package com.gautam.order.dao;

import com.gautam.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface OrderDAO extends JpaRepository<Order,Long> {


    Optional<Order> findByOrderId(Long orderId);

}
