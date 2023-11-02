package com.example.payment.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

/**
 * Application Main.
 *
 */
@ComponentScan(basePackages = { "com.example.payment.iam.config", "com.example.payment.merchant.config" })
@SpringBootApplication()
@Profile("default") // Execute when 'default' profile is active
public class AppWeb {
    /**
     * Main.
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(AppWeb.class, args);
    }
}
