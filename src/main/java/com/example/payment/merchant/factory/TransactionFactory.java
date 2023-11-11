package com.example.payment.merchant.factory;

import java.util.UUID;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.model.TransactionAuthorize;
import com.example.payment.merchant.model.TransactionCharge;
import com.example.payment.merchant.model.TransactionRefund;
import com.example.payment.merchant.model.TransactionReversal;
import com.example.payment.merchant.model.TransactionStatus;
import com.example.payment.merchant.model.TransactionType;

import jakarta.validation.ValidationException;

/**
 * The TransactionFactory interface.
 */
public interface TransactionFactory {

    default UUID getId() {
        return IdUtils.idUUID();
    }

    ValidationException validate(Transaction transaction);

    default boolean isValid(final Transaction transaction) {
        return validate(transaction) == null;
    }

    default Transaction setTransactionIdIfNeeded(final Transaction transaction) {
        if (transaction != null && transaction.getUuid() == null) {
            return transaction.setUuid(getId());
        }
        return transaction;
    }

    default Transaction setTransactionId(final Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return transaction.setUuid(getId());
    }

    /**
     * Build new Transaction. Validate and throw exception if not valid.
     */
    Transaction getTransaction(TransactionType type, UUID uuid, Merchant merchant, Double amount, TransactionStatus status, String customerEmail,
            String customerPhone, Transaction referenceTransaction) throws ValidationException;

    default Transaction getTransaction(final Transaction transaction) throws ValidationException {
        switch (transaction.getType()) {
        case AUTORIZE:
            return getTransactionAuthorize(transaction);
        case CHARGE:
            return getTransactionCharge(transaction);
        case REFUND:
            return getTransactionRefund(transaction);
        case REVERSAL:
            return getTransactionReversal(transaction);
        default:
            throw new ValidationException("TransactionType not supported");
        }
    }

    /**
     * Build new TransactionAuthorize. Validate and throw exception if not valid.
     *
     */
    default TransactionAuthorize getTransactionAuthorize(final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return (TransactionAuthorize) getTransaction(TransactionType.AUTORIZE, uuid, merchant, amount, status, customerEmail, customerPhone,
                referenceTransaction);
    }

    default TransactionAuthorize getTransactionAuthorize(final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return getTransactionAuthorize(getId(), merchant, amount, status, customerEmail, customerPhone, referenceTransaction);
    }

    default TransactionAuthorize getTransactionAuthorize(final Transaction transaction) throws ValidationException {
        return getTransactionAuthorize(transaction.getUuid(), transaction.getMerchant(), transaction.getAmount(), transaction.getStatusType(),
                transaction.getCustomerEmail(), transaction.getCustomerPhone(), transaction.getReferenceTransaction());
    }

    /**
     * Build new TransactionCharge. Validate and throw exception if not valid.
     */
    default TransactionCharge getTransactionCharge(final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return (TransactionCharge) getTransaction(TransactionType.CHARGE, uuid, merchant, amount, status, customerEmail, customerPhone, referenceTransaction);
    }

    default TransactionCharge getTransactionCharge(final Merchant merchant, final Double amount, final TransactionStatus status, final String customerEmail,
            final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return getTransactionCharge(getId(), merchant, amount, status, customerEmail, customerPhone, referenceTransaction);
    }

    default TransactionCharge getTransactionCharge(final Transaction transaction) throws ValidationException {
        return getTransactionCharge(transaction.getUuid(), transaction.getMerchant(), transaction.getAmount(), transaction.getStatusType(),
                transaction.getCustomerEmail(), transaction.getCustomerPhone(), transaction.getReferenceTransaction());
    }

    /**
     * Build new TransactionRefund. Validate and throw exception if not valid.
     *
     */
    default TransactionRefund getTransactionRefund(final UUID uuid, final Merchant merchant, final Double amount, final TransactionStatus status,
            final String customerEmail, final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return (TransactionRefund) getTransaction(TransactionType.REFUND, uuid, merchant, amount, status, customerEmail, customerPhone, referenceTransaction);
    }

    default TransactionRefund getTransactionRefund(final Merchant merchant, final Double amount, final TransactionStatus status, final String customerEmail,
            final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return getTransactionRefund(getId(), merchant, amount, status, customerEmail, customerPhone, referenceTransaction);
    }

    default TransactionRefund getTransactionRefund(final Transaction transaction) throws ValidationException {
        return getTransactionRefund(transaction.getUuid(), transaction.getMerchant(), transaction.getAmount(), transaction.getStatusType(),
                transaction.getCustomerEmail(), transaction.getCustomerPhone(), transaction.getReferenceTransaction());
    }

    /**
     * Build new TransactionReversal. Validate and throw exception if not valid.
     *
     */
    default TransactionReversal getTransactionReversal(final UUID uuid, final Merchant merchant, final TransactionStatus status, final String customerEmail,
            final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return (TransactionReversal) getTransaction(TransactionType.REVERSAL, uuid, merchant, null, status, customerEmail, customerPhone, referenceTransaction);
    }

    default TransactionReversal getTransactionReversal(final Merchant merchant, final TransactionStatus status, final String customerEmail,
            final String customerPhone, final Transaction referenceTransaction) throws ValidationException {
        return getTransactionReversal(getId(), merchant, status, customerEmail, customerPhone, referenceTransaction);
    }

    default TransactionReversal getTransactionReversal(final Transaction transaction) throws ValidationException {
        return getTransactionReversal(transaction.getUuid(), transaction.getMerchant(), transaction.getStatusType(), transaction.getCustomerEmail(),
                transaction.getCustomerPhone(), transaction.getReferenceTransaction());
    }
}
