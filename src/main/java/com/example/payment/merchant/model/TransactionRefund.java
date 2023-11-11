package com.example.payment.merchant.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.ValidationException;

/**
 * Refund transaction - has amount and used to reverse a specific amount (whole
 * amount) of the Charge Transaction and return it to the customer.
 *
 * Transitions the Charge transaction to status refunded The approved Refund
 * transactions will decrease the merchant's total transaction amount
 */
@Entity
@DiscriminatorValue(value = "F")
public final class TransactionRefund extends Transaction {

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

    public TransactionRefund() {
        super(TransactionType.REFUND);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.REFUND;
    }
}
