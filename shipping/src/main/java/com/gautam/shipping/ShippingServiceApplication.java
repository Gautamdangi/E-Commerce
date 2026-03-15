package com.gautam.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableFeignClients
@EnableKafka
@EnableCaching
public class ShippingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShippingServiceApplication.class, args);
	}

}
