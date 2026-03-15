package com.gautam.inventory.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic InventoryCreatedTopic(){
        return new NewTopic("Inventory-created", 1, (short) 1);
    }
}
