package com.gautam.shipping.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentClient {

}
