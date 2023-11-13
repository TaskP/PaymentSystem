package com.example.payment.app.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

/**
 * Application Main.
 *
 */
@ComponentScan(basePackages = { "com.example.payment.app.config", "com.example.payment.merchant.cron" })
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
