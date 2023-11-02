package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.IdUtils;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.model.TransactionAuthorize;

/**
 * TransactionService TransactionAuthorize test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestTransactionServiceTransactionAuthorizeHappy {

    /**
     * TransactionService under test.
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * MerchantService.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * Test create TransactionAuthorize.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create a TransactionAuthorize
     *
     * 3. Delete TransactionAuthorize
     *
     * 4. Delete Merchant
     *
     */
    @Test
    void testCreateTransactionAuthorize() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String email = methodName + "-" + runId + "@" + clazzName + ".test";

        // Create Merchant
        final long merchantId = runId;
        Merchant merchant = new Merchant(merchantId, email, null, email, true);
        merchant = this.merchantService.save(merchant);

        TransactionAuthorize transactionAuthorize = new TransactionAuthorize();
        transactionAuthorize.setMerchant(merchant);
        transactionAuthorize.setAmount(1D);
        transactionAuthorize.setCustomerEmail(email);
        transactionAuthorize = (TransactionAuthorize) transactionService.create(transactionAuthorize);

        Optional<Transaction> optTransaction = this.transactionService.findById(transactionAuthorize.getUuid());
        assertTrue(optTransaction.isPresent(), "findById failed #1");
        assertTrue(optTransaction.get() instanceof TransactionAuthorize, "findById failed #2");
        assertTrue(email.equals(optTransaction.get().getCustomerEmail()), "findById failed #3");

        this.transactionService.deleteById(transactionAuthorize.getUuid());
        optTransaction = this.transactionService.findById(transactionAuthorize.getUuid());
        assertFalse(optTransaction.isPresent(), "Delete transaction failed");

        this.merchantService.deleteById(merchantId);
        final Optional<Merchant> optMerchant = this.merchantService.findById(merchantId);
        assertFalse(optMerchant.isPresent(), "Delete merchant failed");

    }
}
