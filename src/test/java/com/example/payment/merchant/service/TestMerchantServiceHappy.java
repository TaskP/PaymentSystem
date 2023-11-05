package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.Role;
import com.example.payment.merchant.factory.MerchantFactory;
import com.example.payment.merchant.model.Merchant;

/**
 * MerchantService test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantServiceHappy {

    /**
     * MerchantService under test.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * MerchantFactory.
     */
    @Autowired
    private MerchantFactory merchantFactory;

    /**
     * UserFactory.
     */
    @Autowired
    private UserFactory userFactory;

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

        final long merchantId = runId;
        final long userId = merchantId + 1;
        final String merchantName = clazzName + "-" + runId;
        Merchant merchant = merchantFactory.getMerchant(merchantId, merchantName, email);
        merchant.addUser(userFactory.getUser(userId, email, merchantName, Role.MERCHANT));

        System.out.println("Creating " + merchant);

        merchant = this.merchantService.create(merchant);

        System.out.println("Created " + merchant);

        Optional<Merchant> optMerchant = this.merchantService.findById(merchantId);

        System.out.println("FindById:" + optMerchant);

        assertTrue(optMerchant.isPresent(), "findById failed #1");
        assertTrue(merchantName.equals(optMerchant.get().getName()), "findById failed #2");

        optMerchant = this.merchantService.findByName(merchantName);
        assertTrue(optMerchant.isPresent(), "findByName failed #1");
        assertTrue(optMerchant.get().getId() == merchantId, "findByName failed #2");

        optMerchant = this.merchantService.findByUserId(userId);
        assertTrue(optMerchant.isPresent(), "findByUserId failed #1");
        assertTrue(optMerchant.get().getId() == merchantId, "findByUserId failed #2");

        this.merchantService.deleteById(merchantId);
        optMerchant = this.merchantService.findById(merchantId);
        assertFalse(optMerchant.isPresent(), "Delete failed");
    }

}
