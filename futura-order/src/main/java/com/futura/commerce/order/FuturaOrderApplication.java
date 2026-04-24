package com.futura.commerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.futura.commerce.order", "com.futura.commerce.common", "com.futura.commerce.security"})
@EntityScan(basePackages = {"com.futura.commerce.mbg.model"})
@EnableJpaRepositories(basePackages = {"com.futura.commerce.mbg.repository"})
public class FuturaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuturaOrderApplication.class, args);
    }
}
