package com.gautam.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("PAYMENT SERVICE API")
                        .version("1.0")
                        .description("APIs for managing Payments"));

    }
}
