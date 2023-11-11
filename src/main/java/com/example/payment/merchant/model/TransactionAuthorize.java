package com.example.payment.merchant.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.ValidationException;

/**
 * Authorize transaction - has amount and used to hold customer's amount.
 */
@Entity
@DiscriminatorValue(value = "A")
public final class TransactionAuthorize extends Transaction {

    private static final long serialVersionUID = 1L;

    /**
     * Amount can not be null.
     */
    @PrePersist
    @PreUpdate
    private void checkAmountNotNull() throws Exception {
        if (getAmount() == null) {
            throw new ValidationException("Amount is null");
        }
    }

    public TransactionAuthorize() {
        super(TransactionType.AUTORIZE);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.AUTORIZE;
    }
}
