package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.IdUtils;
import com.example.payment.merchant.model.Merchant;

/**
 * MerchantService test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantServiceHappy {

    /**
     * MerchantService under test.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * Test create.
     *
     * Steps:
     *
     * 1. Creates a Merchant
     *
     * 2. Fetches Merchant by Id and compares Name
     *
     * 3. Fetches Merchant by Name and compares Id
     *
     * 3. Deletes Merchant
     */
    @Test
    void testCreateHappy() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String email = methodName + "-" + runId + "@" + clazzName + ".test";

        final long id = runId;
        final String name = clazzName + "-" + runId;
        Merchant merchant = new Merchant(id, name, name + " Description", email, true);
        merchant = this.merchantService.save(merchant);
        Optional<Merchant> optMerchant = this.merchantService.findById(id);
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(name.equals(optMerchant.get().getName()), "findById failed #2");

        optMerchant = this.merchantService.findByName(name);
        assertTrue(optMerchant.isPresent(), "findByName failed #1");
        assertTrue(optMerchant.get().getId() == id, "findByName failed #2");

        this.merchantService.deleteById(id);
        optMerchant = this.merchantService.findById(id);
        assertFalse(optMerchant.isPresent(), "Delete failed");
    }

}
