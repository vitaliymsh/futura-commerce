package com.futura.commerce.seckill.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI documentation configuration for seckill service
 *
 * @author Vitalii
 */
@Configuration
public class SeckillSwaggerConfig {

    @Bean
    public GroupedOpenApi seckillApi() {
        return GroupedOpenApi.builder()
                .group("seckill-service")
                .packagesToScan("com.futura.commerce.seckill.controller")
                .build();
    }

    @Bean
    public OpenAPI seckillOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Seckill Marketing Service API")
                        .description("Flash sale events, coupons, full reduction, and follow discounts")
                        .version("1.0"));
    }
}
