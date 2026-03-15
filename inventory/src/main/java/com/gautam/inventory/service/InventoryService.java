package com.gautam.inventory.service;

import com.gautam.inventory.dao.InventoryDAO;
import com.gautam.inventory.dto.InventoryResponseDTO;
import com.gautam.inventory.dto.ProductResponseDTO;
import com.gautam.inventory.exceptions.InsufficientStockException;
import com.gautam.inventory.exceptions.ResourceNotFoundException;
import com.gautam.inventory.exceptions.ServiceUnavailableException;
import com.gautam.inventory.feign.ProductClient;
import com.gautam.inventory.model.InventoryDetails;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class InventoryService {
    @Autowired
    private InventoryDAO inventoryDAO;
    @Autowired
    private ProductClient productClient;

//adding
    @Transactional
    public InventoryResponseDTO addStock(Long productId,int quantity){

        if(quantity<=0){
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        InventoryDetails inventory = inventoryDAO.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product: " + productId));
        //InventoryDetails inventory = inventoryDAO.findByProductId(productId).orElseGet(()->createNewInventory(productId));
        inventory.setAvailableQuantity(inventory.getAvailableQuantity()+quantity);
        inventory.setUpdatedAt(LocalDateTime.now());

        InventoryDetails saved = inventoryDAO.save(inventory);
        return mapToInventoryResponseDTO(saved);
    }

//order success deduct quantity
    @Transactional
    public InventoryResponseDTO deductStock(Long productId, int quantity){
        InventoryDetails inventory = getInventory(productId);
        if(inventory.getAvailableQuantity() < quantity){
            throw new InsufficientStockException("Not enough stock");
        }
        inventory.setAvailableQuantity(inventory.getAvailableQuantity()-quantity);
        InventoryDetails saved = inventoryDAO.save(inventory);
        return mapToInventoryResponseDTO(saved);
    }

    //order cancelled addback quantity

    @Transactional
    public InventoryResponseDTO addBackStock(Long productId, int quantity){
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        InventoryDetails inventory = getInventory(productId);

        // Prevent returning more than what was reserved/ordered
        if (quantity > inventory.getReserveQuantity()) {
            throw new IllegalArgumentException(
                    "Return quantity (" + quantity + ") cannot exceed reserved quantity ("
                            + inventory.getReserveQuantity() + ")");
        }
        inventory.setAvailableQuantity(inventory.getAvailableQuantity()+quantity);
        inventory.setReserveQuantity(inventory.getReserveQuantity() - quantity); // release the reservation
        inventory.setUpdatedAt(LocalDateTime.now());
        InventoryDetails saved = inventoryDAO.save(inventory);
        return mapToInventoryResponseDTO(saved);
    }


    //check stock
    public int getStock(Long productId){
        return getInventory(productId).getAvailableQuantity();
    }





    private InventoryResponseDTO mapToInventoryResponseDTO(InventoryDetails inventoryDetails){
        return new InventoryResponseDTO(
                inventoryDetails.getId(),
                inventoryDetails.getProductId(),
                inventoryDetails.getProductName(),
                inventoryDetails.getAvailableQuantity(),
                inventoryDetails.getIsActive(),
                inventoryDetails.getUpdatedAt()
        );
    }

    private InventoryDetails getInventory(Long productId){
        return inventoryDAO.findByProductId(productId)
                .orElseThrow(()-> new ResourceNotFoundException("No inventory for product:" +productId));
    }

}
