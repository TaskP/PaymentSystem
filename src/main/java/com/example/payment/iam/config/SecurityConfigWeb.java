package com.example.payment.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.payment.iam.model.Role;

@Configuration
@EnableWebSecurity
@Profile("default") // Execute when 'default' profile is active
public class SecurityConfigWeb {

    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.securityMatcher("/**").authorizeHttpRequests(authorize -> authorize.anyRequest().hasRole(Role.ADMINISTRATOR.getTypeName())).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}