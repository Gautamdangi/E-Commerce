package com.gautam.product.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductEventPublisher {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProductCreated(ProductCreatedEvent event) {
        kafkaTemplate.send("product-created", event);
    }
}
