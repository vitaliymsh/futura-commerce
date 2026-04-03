package com.futura.commerce.search.config;

import com.futura.commerce.common.config.BaseSwaggerConfig;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Search module Swagger OpenAPI configuration
 *
 * @author Vitalii
 */
@Configuration
public class SearchSwaggerConfig extends BaseSwaggerConfig {

    @Bean
    public GroupedOpenApi searchGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Search Module")
                .packagesToScan(getModulePackage())
                .build();
    }

    @Override
    public String getModulePackage() {
        return "com.futura.commerce.search";
    }
}
