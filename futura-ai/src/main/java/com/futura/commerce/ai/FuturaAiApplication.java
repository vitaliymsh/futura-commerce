package com.futura.commerce.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Entry point for Futura AI microservice
 *
 * @author Vitalii
 */
@EnableFeignClients(basePackages = {"com.futura.commerce.feign"})
@SpringBootApplication(scanBasePackages = {
        "com.futura.commerce.ai",
        "com.futura.commerce.common",
        "com.futura.commerce.security"
})
@EntityScan(basePackages = {"com.futura.commerce.mbg.model"})
@EnableJpaRepositories(basePackages = {"com.futura.commerce.mbg.repository"})
public class FuturaAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuturaAiApplication.class, args);
    }
}
