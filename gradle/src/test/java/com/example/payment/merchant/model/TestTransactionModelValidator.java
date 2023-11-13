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
 * Transaction validation test cases.
 *
 * ./gradlew test --tests "TestTransactionModelValidator"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestTransactionModelValidator extends TestMerchantBase {

    /**
     * Validate TransactionAuthorize amount via Local Validator.
     */
    @Test
    void testTransactionAuthorizeLocalValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final Merchant merchant = getMerchant();
        final TransactionAuthorize transaction = getTransactionFactory().getTransactionAuthorize(getTransactionFactory().getId(), merchant, 1D,
                getEmail(methodName));
        final Validator validator = getValidator();
        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        assertThat(constraintViolations).hasSize(0);
        transaction.setAmount(0D);
        constraintViolations = validator.validate(transaction);
        assertThat(constraintViolations).hasSize(1);
    }

    /**
     * Validate TransactionAuthorize amount via TransactionFactory Validator.
     */
    @Test
    void testTransactionAuthorizeFactoryValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final Merchant merchant = getMerchant();
        final TransactionAuthorize transaction = getTransactionFactory().getTransactionAuthorize(getTransactionFactory().getId(), merchant, 1D,
                getEmail(methodName));
        assertDoesNotThrow(() -> getTransactionFactory().validate(transaction));
        transaction.setAmount(0D);
        final Exception exception = assertThrows(Exception.class, () -> getTransactionFactory().validate(transaction));
        assertNotNull(exception);
    }

    /**
     * Validate TransactionReversal via Local Validator.
     */
    @Test
    void testTransactionReversalLocalValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final Merchant merchant = getMerchant();
        final TransactionAuthorize transactionAuthz = getTransactionFactory().getTransactionAuthorize(getTransactionFactory().getId(), merchant, 1D,
                getEmail(methodName));
        final TransactionReversal transaction = getTransactionFactory().getTransactionReversal(getTransactionFactory().getId(), merchant, transactionAuthz);
        final Validator validator = getValidator();
        final Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        assertThat(constraintViolations).hasSize(0);
    }

    /**
     * Validate TransactionReversal via TransactionFactory Validator.
     */
    @Test
    void testTransactionReversalFactoryValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final Merchant merchant = getMerchant();
        final TransactionAuthorize transactionAuthz = getTransactionFactory().getTransactionAuthorize(getTransactionFactory().getId(), merchant, 1D,
                getEmail(methodName));
        final TransactionReversal transaction = getTransactionFactory().getTransactionReversal(getTransactionFactory().getId(), merchant, transactionAuthz);
        assertDoesNotThrow(() -> getTransactionFactory().validate(transaction));
        transaction.setAmount(0D);
        assertNotNull(assertThrows(Exception.class, () -> getTransactionFactory().validate(transaction)));
        transaction.setAmount(null);
        transaction.setReferenceId(null);
        transaction.setReferenceTransaction(null);
        assertNotNull(assertThrows(Exception.class, () -> getTransactionFactory().validate(transaction)));
    }

}
