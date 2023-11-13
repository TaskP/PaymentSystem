package com.example.payment.merchant.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates Role.
 *
 */
public class TransactionTypeValidator implements ConstraintValidator<TransactionTypeValidation, Byte> {

    /**
     * Validate TransactionType by value.
     */
    @Override
    public boolean isValid(final Byte value, final ConstraintValidatorContext context) {
        final TransactionType type = TransactionType.parse(value);
        if (type == null) {
            return false;
        }
        return !TransactionType.UKNOWN.equals(type);
    }

}
