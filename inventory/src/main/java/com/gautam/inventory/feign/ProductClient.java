package com.gautam.inventory.feign;

import com.gautam.inventory.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("PRODUCT-CATALOG")
public interface ProductClient {
    @GetMapping("/productdetails/getById/{id}")
    ProductResponseDTO getProduct(@PathVariable Long id);

}
