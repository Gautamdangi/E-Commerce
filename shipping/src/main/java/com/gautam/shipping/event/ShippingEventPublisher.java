package com.gautam.shipping.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShippingEventPublisher {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public ShippingEventPublisher(KafkaTemplate<String,Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;

    }
    public void publishShipmentDelivered(Object event) {
        kafkaTemplate.send("shipment-events", event);
    }
}
