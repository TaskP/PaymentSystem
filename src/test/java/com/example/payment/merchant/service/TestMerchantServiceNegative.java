package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.Role;
import com.example.payment.merchant.factory.MerchantFactory;
import com.example.payment.merchant.model.Merchant;

import jakarta.validation.ValidationException;

/**
 * MerchantService test cases. Negative scenarios.
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantServiceNegative {

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
     * Test Email validation.
     *
     * Creates a Merchant with invalid email. Email validator throws validation
     * exception.
     *
     * This test case does not use MerchantFactory since it checks for validity and
     * it will not test persistence validation.
     *
     */
    @Test
    void testInvalidEmail() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String emailImvalid = methodName + "-" + runId + "@" + clazzName + " test";

        final long merchantId = runId;
        final String name = clazzName + "-" + runId;
        final Merchant merchant = merchantFactory.getMerchant(merchantId, name);
        merchant.setEmail(emailImvalid);
        merchant.addUser(userFactory.getUser(merchantId, name, name, Role.MERCHANT));
        final Exception exception = assertThrows(Exception.class, () -> this.merchantService.create(merchant));
        assertNotNull(exception);
        assertEquals(ValidationException.class, exception.getClass());
    }
}
