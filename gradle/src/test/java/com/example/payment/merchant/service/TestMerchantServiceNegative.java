package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.merchant.TestMerchantBase;
import com.example.payment.merchant.model.Merchant;

import jakarta.validation.ValidationException;

/**
 * MerchantService test cases. Negative.
 *
 * ./gradlew test --tests "TestMerchantServiceNegative"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantServiceNegative extends TestMerchantBase {

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
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final String emailImvalid = getName() + "@ " + methodName;

        final Merchant merchant = getMerchant(methodName);
        merchant.setEmail(emailImvalid);
        final Exception exception = assertThrows(Exception.class, () -> createMerchant(merchant));
        assertNotNull(exception);
        assertEquals(ValidationException.class, exception.getClass());
    }
}
