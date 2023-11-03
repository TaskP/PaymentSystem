package com.example.payment.iam.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * IAM Spring Boot configuration.
 */
@SpringBootConfiguration
@EntityScan(basePackages = { "com.example.payment.iam.model" })
@ComponentScan(basePackages = { "com.example.payment.iam.controller", "com.example.payment.iam.factory", "com.example.payment.iam.security",
        "com.example.payment.iam.service" })
@EnableJpaRepositories(basePackages = { "com.example.payment.iam.repository" })
public class IamConfig {

}
