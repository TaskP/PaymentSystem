package com.example.payment.merchant.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Merchant Spring Boot configuration. We must not include cron package here in
 * order to avoid scheduling in CLI
 */
@SpringBootConfiguration
@EntityScan(basePackages = { "com.example.payment.merchant.model" })
@ComponentScan(basePackages = { "com.example.payment.merchant.controller", "com.example.payment.merchant.factory", "com.example.payment.merchant.service" })
@EnableJpaRepositories(basePackages = { "com.example.payment.merchant.repository" })
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class ConfigMerchant {

}
