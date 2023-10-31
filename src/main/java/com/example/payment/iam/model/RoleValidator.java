package com.example.payment.iam.model;

import com.example.payment.utils.BitUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates Role.
 *
 */
public class RoleValidator implements ConstraintValidator<RoleValidation, Long> {

    /**
     * Number of bits in Role.
     */
    private static final int BITS = 64;

    /**
     * Checks value bits for valid role bit positions.
     */
    @Override
    public boolean isValid(final Long value, final ConstraintValidatorContext context) {
        if (value == null || value == 0) {
            return false;
        }
        for (int i = 0; i < BITS; i++) {
            if (BitUtils.isSet(i, value)) {
                if (!Role.isValid((byte) i)) {
                    return false;
                }
            }
        }
        return true;
    }

}
