package com.example.payment.merchant.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.merchant.TestMerchantBase;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * Merchant validation test cases.
 *
 * ./gradlew test --tests "TestMerchantModelValidator"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestMerchantModelValidator extends TestMerchantBase {

    /**
     * Validate Merchant email via Local Validator.
     */
    @Test
    void testMerchantLocalValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final Validator validator = getValidator();
        final Merchant merchant = getMerchant(methodName);
        Set<ConstraintViolation<Merchant>> constraintViolations = validator.validate(merchant);
        assertThat(constraintViolations).hasSize(0);
        merchant.setEmail("some invalid email");
        constraintViolations = validator.validate(merchant);
        assertThat(constraintViolations).hasSize(1);
    }

    /**
     * Validate Merchant email via MerchantFactory Validator.
     */
    @Test
    void testMerchantFactoryValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final Merchant merchant = getMerchant(methodName);
        assertDoesNotThrow(() -> getMerchantFactory().getValidationException(merchant));
        merchant.setEmail("some invalid email");
        final Exception exception = assertThrows(Exception.class, () -> getMerchantFactory().validate(merchant));
        assertNotNull(exception);
    }

}
