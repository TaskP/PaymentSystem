package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.merchant.TestMerchantBase;
import com.example.payment.merchant.model.Merchant;

/**
 * MerchantService test cases. Happy path.
 *
 * ./gradlew test --tests "TestMerchantServiceHappy"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantServiceHappy extends TestMerchantBase {

    /**
     * Test create.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Fetch Merchant by Id and compares Name
     *
     * 3. Fetch Merchant by Name and compares Id
     *
     * 4. Delete Merchant
     */
    @Test
    void testCreateHappy() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on
        final Merchant merchant = createMerchant(methodName);
        Optional<Merchant> optMerchant = getMerchantService().findById(getRunId());
        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchant.getName().equals(optMerchant.get().getName()), "findById failed #2");

        optMerchant = this.getMerchantService().findByName(merchant.getName());
        assertTrue(optMerchant.isPresent(), "findByName failed #1");
        assertTrue(optMerchant.get().getId() == getRunId(), "findByName failed #2");

        optMerchant = this.getMerchantService().findByUserId(getRunId());
        assertTrue(optMerchant.isPresent(), "findByUserId failed #1");
        assertTrue(optMerchant.get().getId() == getRunId(), "findByUserId failed #2");

        deleteMerchantCascade(merchant);
        optMerchant = this.getMerchantService().findById(getRunId());
        assertFalse(optMerchant.isPresent(), "Delete failed");
    }

}
