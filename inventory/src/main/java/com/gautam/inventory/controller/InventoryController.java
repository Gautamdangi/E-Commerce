package com.gautam.inventory.controller;

import com.gautam.inventory.dto.AddStockRequest;
import com.gautam.inventory.dto.InventoryResponseDTO;
import com.gautam.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "inventory",description = "APIs for managing inventory")
public class InventoryController {

    @Autowired
            private InventoryService inventoryService;

    @Operation(summary = "Add stock in the inventory")
    @PostMapping("/addstock")
    public ResponseEntity<InventoryResponseDTO> addStock(@RequestBody AddStockRequest request){
        InventoryResponseDTO response=  inventoryService.addStock(request.getProductId(),request.getQuantity());
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Deduct stock from the inventory")
    @PostMapping("/deductStock/{productId}")
    public ResponseEntity<InventoryResponseDTO> deductStock(@PathVariable Long productId, @RequestParam int quantity){
        InventoryResponseDTO responseDTO = inventoryService.deductStock(productId,quantity);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "AddBack stock in the inventory")
    @PostMapping("/addBackStock/{productId}")
    public ResponseEntity<InventoryResponseDTO> addBackStock(@PathVariable Long productId, @RequestParam int quantity){
        InventoryResponseDTO responseDTO = inventoryService.addBackStock(productId, quantity);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Get stock in the inventory")
    @GetMapping("/getstock/{productId}")
    public int getStock(@PathVariable Long productId){
        return inventoryService.getStock(productId);
    }
}
