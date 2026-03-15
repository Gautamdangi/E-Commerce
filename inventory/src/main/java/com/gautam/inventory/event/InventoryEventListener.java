package com.gautam.inventory.event;

import com.gautam.inventory.dao.InventoryDAO;
import com.gautam.inventory.model.InventoryDetails;
import com.gautam.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class InventoryEventListener {
    @Autowired
    private final InventoryDAO inventoryDAO;
    @Autowired
    InventoryService inventoryService;


    public InventoryEventListener(InventoryDAO inventoryDAO){
        this.inventoryDAO =inventoryDAO;
    }
    @KafkaListener(topics = "product-created", groupId = "inventory-group")
    public void handleProductCreated(ProductCreatedEvent event) {

        InventoryDetails inventory = new InventoryDetails();
        inventory.setProductId(event.getProductId());
        inventory.setProductName(event.getName());
        inventory.setAvailableQuantity(event.getQuantity());
        inventory.setAvailableQuantity(0);
        inventory.setReserveQuantity(0);
        inventory.setIsActive(true);
        inventory.setCreatedAt(LocalDateTime.now());

        inventoryDAO.save(inventory);

        log.info("Inventory created for product:{}", inventory.getId());

        System.out.println("Inventory created for product: " + event.getProductId());
    }
    @KafkaListener(topics = "order-cancelled", groupId = "inventory-group")
    public void handleOrderCancel(OrderCancelEvent event){
        for(OrderItemEvent item : event.getItemEvents()){

            inventoryService.addBackStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }
}
}
