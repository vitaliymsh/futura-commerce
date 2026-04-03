package com.futura.commerce.security.config;

import com.futura.commerce.common.config.BaseSwaggerConfig;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Security module Swagger OpenAPI configuration
 *
 * @author Vitalii
 */
@Configuration
public class SwaggerConfig extends BaseSwaggerConfig {

    @Bean
    public GroupedOpenApi securityGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Security Module")
                .packagesToScan(getModulePackage())
                .build();
    }

    @Override
    public String getModulePackage() {
        return "com.futura.commerce.security";
    }
}
