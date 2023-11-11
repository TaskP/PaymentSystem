package com.example.payment.merchant.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.ValidationException;

/**
 * Charge transaction - has amount and used to confirm the amount is taken from
 * the customer's account and transferred to the merchant.
 *
 * The merchant's total transactions amount has to be the sum of the approved
 * Charge transactions.
 *
 */
@Entity
@DiscriminatorValue(value = "C")
public final class TransactionCharge extends Transaction {

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

    public TransactionCharge() {
        super(TransactionType.CHARGE);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.CHARGE;
    }

}
