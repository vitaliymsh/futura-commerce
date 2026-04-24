package com.futura.commerce.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI documentation configuration for product service
 *
 * @author Vitalii
 */
@Configuration
public class ProductSwaggerConfig {

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("product-service")
                .packagesToScan("com.futura.commerce.product.controller")
                .build();
    }

    @Bean
    public OpenAPI productOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service API")
                        .description("Product catalog, SKU, categories, pricing, and promotion management")
                        .version("1.0"));
    }
}
