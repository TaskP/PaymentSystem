package com.example.payment.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.service.MerchantService;

/**
 * Import new merchants from CSV File.
 *
 * Format: Column 1 - Name, Column 2 - Description, Column 3 - Email, Column 4
 * (Optional) - Status
 *
 * Status of newly imported merchants is active unless there is Column 4 with
 * case insensitive "false" or "inactive"
 *
 * Profile setting spring.profiles.active=cli is required
 */
@ComponentScan(basePackages = { "com.example.payment.merchant.config" })
@SpringBootApplication
@Profile("cli")
public class AppCliMerchantImport extends Importer implements CommandLineRunner {

    /**
     * Merchant service.
     *
     * @see com.example.payment.merchant.service.MerchantService
     *
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * Main.
     *
     * @param args CSV File to load
     */
    public static void main(final String[] args) {
        SpringApplication.run(AppCliMerchantImport.class, args);
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

        int r = 0;
        for (final String[] row : csvList) {
            r++;
            if (row == null || row.length < 3) {
                throw new IllegalArgumentException("Error: 2010 - Invalid argument! Invalid data on row " + r);
            }
            final Merchant merchant = new Merchant();
            merchant.setStatus(true);
            merchant.setName(row[0]);
            merchant.setDescription(row[1]);
            merchant.setEmail(row[2]);
            if (row.length > 3) {
                if ("false".equalsIgnoreCase(row[3]) || "inactive".equalsIgnoreCase(row[3])) {
                    merchant.setStatus(false);
                }
            }
            merchantService.save(merchant);
        }
    }
}
