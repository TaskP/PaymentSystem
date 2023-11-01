package com.example.payment.iam.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.payment.iam.model.Role;

/**
 * The SecurityConfigWeb class configures security settings and access control
 * for the web application.
 *
 */
@Configuration
@EnableWebSecurity
@EnableWebMvc
@Profile("default") // Execute when 'default' profile is active
public class SecurityConfigWeb {

    /**
     * Creates a chain of security filters.
     *
     * CSRF is disabled for /api/
     *
     * @param http current HttpSecurity
     * @return SecurityFilterChain
     */
    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        // @formatter:off
        // @formatter:on
        http.authorizeHttpRequests((authz) -> authz.requestMatchers("/api/merchant**", "/ui/merchant**").hasAuthority(Role.MERCHANT.getRoleName())
                .requestMatchers("/api/user**", "/ui/user/**").hasAuthority(Role.ADMINISTRATOR.getRoleName()).requestMatchers("/").permitAll()
                .requestMatchers("/login").permitAll().anyRequest().authenticated()).formLogin(withDefaults()).httpBasic(withDefaults())
                .csrf((csrf) -> csrf.ignoringRequestMatchers("/api/merchant**", "/api/user**"));
        return http.build();
    }
}
