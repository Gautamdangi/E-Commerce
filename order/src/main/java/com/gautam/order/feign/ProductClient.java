package com.gautam.order.feign;

import com.gautam.order.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("PRODUCT-CATALOG")
public interface ProductClient {
    @GetMapping("/productdetails/getById/{id}")
    ProductResponse getProduct(@PathVariable Long id);

}
