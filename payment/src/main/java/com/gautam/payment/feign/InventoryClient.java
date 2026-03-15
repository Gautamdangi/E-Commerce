package com.gautam.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryClient {
}
