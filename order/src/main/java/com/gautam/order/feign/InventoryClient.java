package com.gautam.order.feign;

import com.gautam.order.dto.InventoryResponse;
import com.gautam.order.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryClient {
    @GetMapping("/api/inventory/getstock/{productId}")
    InventoryResponse getStock(@PathVariable Long productId);

    @PostMapping("/api/inventory/deductStock/{productId}")
    InventoryResponse deductStock(@PathVariable Long productId, @RequestParam int quantity);

    @PostMapping("/api/inventory/addBackStock/{productId}")
    InventoryResponse  addBackStock(@PathVariable Long productId, @RequestParam int quantity);
}
