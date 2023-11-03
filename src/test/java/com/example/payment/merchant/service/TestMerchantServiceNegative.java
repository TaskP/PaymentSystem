package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import com.example.payment.common.IdUtils;
import com.example.payment.merchant.model.Merchant;

/**
 * MerchantService test cases. Negative scenarios.
 */
@SpringBootTest(classes = com.example.payment.app.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantServiceNegative {

    /**
     * MerchantService under test.
     */
    @Autowired
    private MerchantService merchantService;

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
        final long id = IdUtils.idLong();
        final String name = "testInvalidEmail-" + id;
        final Merchant user = new Merchant(id, name, name + " Description", "example@not valid.com", true);
        final Exception exception = assertThrows(Exception.class, () -> merchantService.save(user));
        assertNotNull(exception);
        assertEquals(TransactionSystemException.class, exception.getClass());
    }
}
