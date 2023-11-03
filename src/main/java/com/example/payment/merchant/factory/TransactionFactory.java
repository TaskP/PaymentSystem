package com.example.payment.merchant.factory;

import java.util.UUID;

import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.model.TransactionAuthorize;
import com.example.payment.merchant.model.TransactionCharge;
import com.example.payment.merchant.model.TransactionRefund;
import com.example.payment.merchant.model.TransactionReversal;
import com.example.payment.merchant.model.TransactionStatus;
import com.example.payment.merchant.model.TransactionType;

/**
 * The TransactionFactory interface.
 */
public interface TransactionFactory {

    /**
     * Build new Transaction. Validate and throw exception if not valid.
     *
     * @param type
     * @param uuid
     * @param merchant
     * @param amount
     * @param status
     * @param customerEmail
     * @param customerPhone
     * @param referenceTransaction
     * @return Transaction
     * @throws IllegalArgumentException
     */
    Transaction getTransaction(TransactionType type, UUID uuid, Merchant merchant, Double amount, TransactionStatus status, String customerEmail,
            String customerPhone, Transaction referenceTransaction) throws IllegalArgumentException;

    /**
     * Build new TransactionAuthorize. Validate and throw exception if not valid.
     *
     * @param uuid
     * @param merchant
     * @param amount
     * @param status
     * @param customerEmail
     * @param customerPhone
     * @param referenceTransaction
     * @return Transaction
     * @throws IllegalArgumentException
     */
    default TransactionAuthorize getTransactionAuthorize(final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws IllegalArgumentException {
        return (TransactionAuthorize) getTransaction(TransactionType.AUTORIZE, uuid, merchant, amount, status, customerEmail, customerPhone,
                referenceTransaction);
    }

    /**
     * Build new TransactionCharge. Validate and throw exception if not valid.
     *
     * @param uuid
     * @param merchant
     * @param amount
     * @param status
     * @param customerEmail
     * @param customerPhone
     * @param referenceTransaction
     * @return Transaction
     * @throws IllegalArgumentException
     */
    default TransactionCharge getTransactionCharge(final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws IllegalArgumentException {
        return (TransactionCharge) getTransaction(TransactionType.CHARGE, uuid, merchant, amount, status, customerEmail, customerPhone, referenceTransaction);
    }

    /**
     * Build new TransactionRefund. Validate and throw exception if not valid.
     *
     * @param uuid
     * @param merchant
     * @param amount
     * @param status
     * @param customerEmail
     * @param customerPhone
     * @param referenceTransaction
     * @return Transaction
     * @throws IllegalArgumentException
     */
    default TransactionRefund getTransactionRefund(final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws IllegalArgumentException {
        return (TransactionRefund) getTransaction(TransactionType.REFUND, uuid, merchant, amount, status, customerEmail, customerPhone, referenceTransaction);
    }

    /**
     * Build new TransactionReversal. Validate and throw exception if not valid.
     *
     * @param uuid
     * @param merchant
     * @param status
     * @param customerEmail
     * @param customerPhone
     * @param referenceTransaction
     * @return Transaction
     * @throws IllegalArgumentException
     */
    default TransactionReversal getTransactionReversal(final UUID uuid, final Merchant merchant, final TransactionStatus status, final String customerEmail,
            final String customerPhone, final Transaction referenceTransaction) throws IllegalArgumentException {
        return (TransactionReversal) getTransaction(TransactionType.REVERSAL, uuid, merchant, null, status, customerEmail, customerPhone, referenceTransaction);
    }

}
