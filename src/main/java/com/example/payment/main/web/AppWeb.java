package com.example.payment.main.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * The @SpringBootApplication is @Configuration, @EnableAutoConfiguration, and @ComponentScan
 * @ComponentScan looks for @Configuration and @Component classes within the same package and all sub-packages hence cli and web must be in different packages
 */
@EntityScan(basePackages = { "com.example.payment.iam.model" })
@ComponentScan(basePackages = { "com.example.payment.iam" })
@EnableJpaRepositories(basePackages = { "com.example.payment.iam.repository" })
@SpringBootApplication
@Profile("default") // Execute when 'default' profile is active
public class AppWeb {

    public static void main(final String[] args) {
        SpringApplication.run(AppWeb.class, args);
    }

}
