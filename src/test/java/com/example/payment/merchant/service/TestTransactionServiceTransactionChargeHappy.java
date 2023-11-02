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
import com.example.payment.merchant.model.TransactionCharge;

/**
 * TransactionService TransactionCharge test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestTransactionServiceTransactionChargeHappy {

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
     * Test create TransactionCharge.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create a TransactionCharge
     *
     * 3. Delete TransactionCharge
     *
     * 4. Delete Merchant
     *
     */
    @Test
    void testCreateTransactionCharge() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String email = methodName + "-" + runId + "@" + clazzName + ".test";

        // Create Merchant
        final long merchantId = runId;
        Merchant merchant = new Merchant(merchantId, email, null, email, true);
        merchant = this.merchantService.save(merchant);

        TransactionCharge transactionCharge = new TransactionCharge();
        transactionCharge.setMerchant(merchant);
        transactionCharge.setAmount(1D);
        transactionCharge.setCustomerEmail(email);
        transactionCharge = (TransactionCharge) transactionService.create(transactionCharge);

        Optional<Transaction> optTransaction = this.transactionService.findById(transactionCharge.getUuid());
        assertTrue(optTransaction.isPresent(), "findById failed #1");
        assertTrue(optTransaction.get() instanceof TransactionCharge, "findById failed #2");
        assertTrue(email.equals(optTransaction.get().getCustomerEmail()), "findById failed #3");

        this.transactionService.deleteById(transactionCharge.getUuid());
        optTransaction = this.transactionService.findById(transactionCharge.getUuid());
        assertFalse(optTransaction.isPresent(), "Delete transaction failed");

        this.merchantService.deleteById(merchantId);
        final Optional<Merchant> optMerchant = this.merchantService.findById(merchantId);
        assertFalse(optMerchant.isPresent(), "Delete merchant failed");

    }
}
