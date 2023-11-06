package com.example.payment.app.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * AppWeb Spring Boot configuration.
 */
@SpringBootConfiguration
@ComponentScan(basePackages = { "com.example.payment.app.controller", "com.example.payment.iam.config", "com.example.payment.merchant.config" })
public class AppWebConfig {

}
