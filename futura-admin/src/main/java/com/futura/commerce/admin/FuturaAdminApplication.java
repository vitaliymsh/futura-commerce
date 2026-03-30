package com.futura.commerce.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Vitalii
 * @date 2026/3/30
 **/
@EnableFeignClients(basePackages = {"com.futura.commerce.feign"})
@SpringBootApplication(scanBasePackages = {"com.futura.commerce.admin", "com.futura.commerce.common", "com.futura.commerce.security"})
@EntityScan(basePackages = {"com.futura.commerce.mbg.model"})
@EnableJpaRepositories(basePackages = {"com.futura.commerce.mbg.repository"})
public class FuturaAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuturaAdminApplication.class, args);
    }

}
