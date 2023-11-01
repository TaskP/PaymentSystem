package com.example.payment.merchant.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Merchant Spring Boot configuration.
 */
@SpringBootConfiguration
@EntityScan(basePackages = { "com.example.payment.merchant.model" })
@ComponentScan(basePackages = { "com.example.payment.merchant.controller", "com.example.payment.merchant.service" })
@EnableJpaRepositories(basePackages = { "com.example.payment.merchant.repository" })
public class ConfigMerchant {

}