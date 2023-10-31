package com.example.payment.main;

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

/**
 * Import new users from CSV File.
 *
 * Format: Column 1 - Username, Column 2 - Full name, Column 3 - Password,
 * Column 4 - Role
 *
 * Status of newly imported users is active
 *
 * Profile setting spring.profiles.active=cli is required
 */
@EntityScan(basePackages = { "com.example.payment.iam.model" })
@ComponentScan(basePackages = { "com.example.payment.iam" })
@EnableJpaRepositories(basePackages = { "com.example.payment.iam.repository" })
@SpringBootApplication
@Profile("cli")
public class AppCliUserImport extends Importer implements CommandLineRunner {

    /**
     * User service.
     *
     * @see com.example.payment.iam.service.UserService
     *
     */
    @Autowired
    private UserService userService;

    /**
     * Main.
     *
     * @param args CSV File to load
     */
    public static void main(final String[] args) {
        SpringApplication.run(AppCliUserImport.class, args);
    }

    /**
     * Entry point for the import.
     *
     * @param args CSV File to load
     * @throws IllegalArgumentException
     */
    @Override
    public void run(final String... args) throws IllegalArgumentException {
        final List<String[]> csvList = loadFile(args);

        final int ixUsername = 0;
        final int ixFullName = 1;
        final int ixPassword = 2;
        final int ixRole = 3;
        final int ixLenght = 4;

        int r = 0;
        for (final String[] row : csvList) {
            r++;
            if (row == null || row.length != ixLenght) {
                throw new IllegalArgumentException("Error: 2010 - Invalid argument! Invalid data on row " + r);
            }
            final User user = new User();
            user.setStatus(true);
            user.setUsername(row[ixUsername]);
            user.setFullName(row[ixFullName]);
            user.setPassword(row[ixPassword]);
            try {
                user.setRole(Long.parseLong(row[ixRole]));
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("Error: 2010 - Invalid argument! Invalid role on row " + r, e);
            }
            userService.create(user);
        }
    }
}
