package com.example.payment.iam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.example.payment.iam.model.Role;

/**
 * The SecurityConfigWeb class configures security settings and access control
 * for the web application.
 *
 */
@Configuration
@EnableWebSecurity
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

        http.authorizeHttpRequests(
                (authorize) -> authorize
                .requestMatchers("/api/merchant**").hasAuthority(Role.MERCHANT.getRoleName())
                .requestMatchers("/api/user**").hasAuthority(Role.ADMINISTRATOR.getRoleName())
                .requestMatchers("/ui/merchant**").hasAuthority(Role.MERCHANT.getRoleName())
                .requestMatchers("/ui/user/**").hasAuthority(Role.ADMINISTRATOR.getRoleName())
                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

                //.csrf((csrf) -> csrf.ignoringRequestMatchers("/api/*"))
                //;
        /*
        http.authorizeHttpRequests(
                (authorize) -> authorize
                .requestMatchers("/ui/merchant**").hasAuthority(Role.MERCHANT.getRoleName())
                .requestMatchers("/ui/user/**").hasAuthority(Role.ADMINISTRATOR.getRoleName())
                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                ;

        http.authorizeHttpRequests(
                (authorize) -> authorize
                    .anyRequest()
                    .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
        ;
*/
        return http.build();
         //@formatter:on

    }
}
