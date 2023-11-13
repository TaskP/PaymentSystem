package com.example.payment.merchant.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * TransactionAmount Validator.
 *
 */
public class TransactionAmountValidator implements ConstraintValidator<TransactionAmountValidation, Double> {

    /**
     * Whether amount can be null.
     */
    private boolean allowsNull = true;

    /**
     * Initialize validator with values from annotation.
     */
    @Override
    public void initialize(final TransactionAmountValidation param) {
        this.allowsNull = param.allowsNull();
    }

    /**
     * Validate Amount.
     */
    @Override
    public boolean isValid(final Double value, final ConstraintValidatorContext context) {
        if (value == null) {
            if (this.allowsNull) {
                return true;
            }
            return false;
        }
        return value > 0;
    }

}
