package com.example.payment.merchant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import com.example.payment.merchant.model.Merchant;
import com.example.payment.utils.IdUtils;

/**
 * MerchantService test cases.
 */
@SpringBootTest(classes = com.example.payment.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantService {

    /**
     * MerchantService under test.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * Test create. Happy path.
     */
    @Test
    void testCreateHappy() {
        final long id = IdUtils.idLong();
        final String name = "testCreateHappy-" + id;
        Merchant merchant = new Merchant(id, name, name + " Description", "example@example.com", true);
        merchant = this.merchantService.save(merchant);
        Optional<Merchant> optMerchant = this.merchantService.findById(id);
        assertTrue(optMerchant.isPresent(), "findById failed");
        assertThat(optMerchant.get().getName()).isEqualTo(name);

        optMerchant = this.merchantService.findByName(name);
        assertTrue(optMerchant.isPresent(), "findByName failed");
        assertThat(optMerchant.get().getName()).isEqualTo(name);

        this.merchantService.deleteById(id);
        optMerchant = this.merchantService.findById(id);
        assertFalse(optMerchant.isPresent(), "Delete failed");
    }

    /**
     * Test create negative. Email validator throws validation exception.
     */
    @Test
    void testCreateNegativeInvalidEmail() {
        final long id = IdUtils.idLong();
        final String name = "testCreateNegativeInvalidEmail-" + id;
        final Merchant user = new Merchant(id, name, name + " Description", "example@not valid.com", true);
        final Exception exception = assertThrows(Exception.class, () -> merchantService.save(user));
        assertNotNull(exception);
        assertEquals(TransactionSystemException.class, exception.getClass());
    }
}
