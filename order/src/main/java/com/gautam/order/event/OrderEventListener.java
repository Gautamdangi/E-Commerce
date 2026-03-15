package com.gautam.order.event;

import com.gautam.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderEventListener {

    @Autowired private OrderService orderService;

    @KafkaListener(topics = "order-paid",groupId = "order-group")
    public void handleOrderPaid(OrderPaidEvent event){
        orderService.markOrderPaid(event.getOrderId());
    }


}
