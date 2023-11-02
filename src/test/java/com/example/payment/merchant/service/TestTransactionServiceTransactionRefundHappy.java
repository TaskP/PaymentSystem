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
import com.example.payment.merchant.model.TransactionRefund;

/**
 * TransactionService TransactionRefund test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestTransactionServiceTransactionRefundHappy {

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
     * Test create TransactionRefund.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create a TransactionRefund
     *
     * 3. Delete TransactionRefund
     *
     * 4. Delete Merchant
     *
     */
    @Test
    void testCreateTransactionRefund() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String email = methodName + "-" + runId + "@" + clazzName + ".test";

        // Create Merchant
        final long merchantId = runId;
        Merchant merchant = new Merchant(merchantId, email, null, email, true);
        merchant = this.merchantService.save(merchant);

        TransactionRefund transactionRefund = new TransactionRefund();
        transactionRefund.setMerchant(merchant);
        transactionRefund.setAmount(1D);
        transactionRefund.setCustomerEmail(email);
        transactionRefund = (TransactionRefund) transactionService.create(transactionRefund);

        Optional<Transaction> optTransaction = this.transactionService.findById(transactionRefund.getUuid());
        assertTrue(optTransaction.isPresent(), "findById failed #1");
        assertTrue(optTransaction.get() instanceof TransactionRefund, "findById failed #2");
        assertTrue(email.equals(optTransaction.get().getCustomerEmail()), "findById failed #3");

        this.transactionService.deleteById(transactionRefund.getUuid());
        optTransaction = this.transactionService.findById(transactionRefund.getUuid());
        assertFalse(optTransaction.isPresent(), "Delete transaction failed");

        this.merchantService.deleteById(merchantId);
        final Optional<Merchant> optMerchant = this.merchantService.findById(merchantId);
        assertFalse(optMerchant.isPresent(), "Delete merchant failed");

    }
}
