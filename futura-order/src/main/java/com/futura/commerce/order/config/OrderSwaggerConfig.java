package com.futura.commerce.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI documentation configuration for order service
 *
 * @author Vitalii
 */
@Configuration
public class OrderSwaggerConfig {

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("order-service")
                .packagesToScan("com.futura.commerce.order.controller")
                .build();
    }

    @Bean
    public OpenAPI orderOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order & Logistics Service API")
                        .description("Order fulfillment, order items, logistics dispatch, tracking, and carrier management")
                        .version("1.0"));
    }
}
