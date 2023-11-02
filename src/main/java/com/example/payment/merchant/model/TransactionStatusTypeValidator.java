package com.example.payment.merchant.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates Role.
 *
 */
public class TransactionStatusTypeValidator implements ConstraintValidator<TransactionStatusTypeValidation, Byte> {

    /**
     * Validate TransactionStatusType by StatusId.
     */
    @Override
    public boolean isValid(final Byte value, final ConstraintValidatorContext context) {
        return TransactionStatusType.parse(value) != null;
    }

}
