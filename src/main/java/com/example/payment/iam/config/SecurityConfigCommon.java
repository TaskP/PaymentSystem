package com.example.payment.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Provides common security configurations for both CLI and WEB application.
 */
@Configuration
public class SecurityConfigCommon {

    /**
     * Creates PasswordEncoder.
     *
     * @return BCryptPasswordEncoder
     */

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
