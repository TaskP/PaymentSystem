package com.example.payment.merchant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.merchant.factory.MerchantFactory;
import com.example.payment.merchant.factory.TransactionFactory;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.model.TransactionReversal;
import com.example.payment.merchant.model.TransactionStatus;

/**
 * TransactionService TransactionReversal test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestTransactionServiceTransactionReversalHappy {

    /**
     * TransactionService under test.
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * TransactionFactory.
     */
    @Autowired
    private TransactionFactory transactionFactory;

    /**
     * MerchantService.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * MerchantFactory.
     */
    @Autowired
    private MerchantFactory merchantFactory;

    /**
     * Test create TransactionReversal.
     *
     * Steps:
     *
     * 1. Create a Merchant
     *
     * 2. Create a TransactionReversal
     *
     * 3. Delete TransactionReversal
     *
     * 4. Delete Merchant
     *
     */
    @Test
    void testCreateTransactionReversal() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String email = methodName + "-" + runId + "@" + clazzName + ".test";

        final long merchantId = runId;
        final String name = clazzName + "-" + runId;
        Merchant merchant = merchantFactory.getMerchant(merchantId, name, email);
        merchant = this.merchantService.create(merchant);

        TransactionReversal transactionReversal = transactionFactory.getTransactionReversal(null, merchant, TransactionStatus.APPROVED, email, null, null);
        transactionReversal = (TransactionReversal) transactionService.create(transactionReversal);

        Optional<Transaction> optTransaction = this.transactionService.findById(transactionReversal.getUuid());
        assertTrue(optTransaction.isPresent(), "findById failed #1");
        assertTrue(optTransaction.get() instanceof TransactionReversal, "findById failed #2");
        assertTrue(email.equals(optTransaction.get().getCustomerEmail()), "findById failed #3");

        this.transactionService.deleteById(transactionReversal.getUuid());
        optTransaction = this.transactionService.findById(transactionReversal.getUuid());
        assertFalse(optTransaction.isPresent(), "Delete transaction failed");

        this.merchantService.deleteById(merchantId);
        final Optional<Merchant> optMerchant = this.merchantService.findById(merchantId);
        assertFalse(optMerchant.isPresent(), "Delete merchant failed");

    }
}
