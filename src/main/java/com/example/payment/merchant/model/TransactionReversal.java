package com.example.payment.merchant.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.ValidationException;

/**
 * Reversal transaction - has no amount, used to invalidate the Authorize
 * Transaction.
 *
 * Transitions the Authorize transaction to status reversed
 *
 */
@Entity
@DiscriminatorValue(value = "V")
public final class TransactionReversal extends Transaction {

    private static final long serialVersionUID = 1L;

    /**
     * Amount must be null.
     */
    @PrePersist
    @PreUpdate
    private void checkAmountNotNull() throws Exception {
        if (getAmount() != null) {
            throw new ValidationException("Amount must be null");
        }
    }

    public TransactionReversal() {
        super(TransactionType.REVERSAL);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.REVERSAL;
    }

}
