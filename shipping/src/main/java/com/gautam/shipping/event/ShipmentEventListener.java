package com.gautam.shipping.event;

import com.gautam.shipping.service.ShippingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShipmentEventListener {

    private ShippingService shippingService;

    @KafkaListener(topics = "order-paid",groupId = "shipping-group")
    private void handleOrderPaid(OrderPaidEvent event){
        shippingService.createShipment(event.getOrderId());
    }
}
