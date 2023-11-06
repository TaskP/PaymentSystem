package com.example.payment.app.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import com.example.payment.common.utils.CsvUtils;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;

/**
 * Import new users from CSV File.
 *
 * Format: Column 1 - Username, Column 2 - Full name, Column 3 - Password,
 * Column 4 - Role, Column 5 (Optional) - Status
 *
 * Status of newly imported users is active unless there is Column 5 with case
 * insensitive "false" or "inactive"
 *
 * Profile setting spring.profiles.active=cli is required
 */
@ComponentScan(basePackages = { "com.example.payment.iam.config" })
@SpringBootApplication
@Profile("cli")
public class AppCliUserImport implements CommandLineRunner {

    /**
     * UserFactory.
     */
    @Autowired
    private UserFactory userFactory;

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
        final List<String[]> csvList = CsvUtils.loadFile(args);

        final int ixUsername = 0;
        final int ixFullName = 1;
        final int ixPassword = 2;
        final int ixRole = 3;
        final int ixStatus = 4;
        int r = 0;
        for (final String[] row : csvList) {
            r++;
            if (row == null || row.length <= ixRole) {
                throw new IllegalArgumentException("Error: 2010 - Invalid argument! Invalid data on row " + r);
            }
            final User user = userFactory.getUserWithId();
            user.setStatus(true);
            user.setUsername(row[ixUsername]);
            user.setFullName(row[ixFullName]);
            user.setPassword(row[ixPassword]);
            if (row.length > ixStatus) {
                if ("false".equalsIgnoreCase(row[ixStatus]) || "inactive".equalsIgnoreCase(row[ixStatus])) {
                    user.setStatus(false);
                }
            }
            try {
                user.setRole(Long.parseLong(row[ixRole]));
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("Error: 2010 - Invalid argument! Invalid role on row " + r, e);
            }
            userService.create(user);
        }
    }
}
