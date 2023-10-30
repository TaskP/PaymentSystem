package com.example.payment.main.cli.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;
import com.example.payment.main.cli.Importer;

/*
 * The @SpringBootApplication is @Configuration, @EnableAutoConfiguration, and @ComponentScan
 * @ComponentScan looks for @Configuration and @Component classes within the same package and all sub-packages hence cli and web must be in different packages
 */
@EntityScan(basePackages = { "com.example.payment.iam.model" })
@ComponentScan(basePackages = { "com.example.payment.iam" })
@EnableJpaRepositories(basePackages = { "com.example.payment.iam.repository" })
@SpringBootApplication
@Profile("cli") // Execute when 'cli' profile is active
public class AppCliUserImport extends Importer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(final String[] args) {
        SpringApplication.run(AppCliUserImport.class, args);
    }

    @Override
    public void run(final String... args) throws IllegalArgumentException {
        final List<String[]> csvList = loadFile(args);
        int r = 0;
        for (final String[] row : csvList) {
            r++;
            if (row == null || row.length != 4) {
                throw new IllegalArgumentException("Error: 2010 - Invalid argument! Invalid data on row " + r);
            }
            final User user = new User();
            user.setStatus(true);
            user.setUsername(row[0]);
            user.setFullName(row[1]);
            user.setPassword(row[2]);
            try {
                user.setRole(Long.parseLong(row[3]));
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("Error: 2010 - Invalid argument! Invalid role on row " + r, e);
            }
            userService.create(user);
        }
    }
}
