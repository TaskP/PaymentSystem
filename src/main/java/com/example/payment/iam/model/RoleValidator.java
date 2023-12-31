package com.example.payment.iam.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates Role.
 *
 */
public class RoleValidator implements ConstraintValidator<RoleValidation, Byte> {

    /**
     * Checks for valid role.
     */
    @Override
    public boolean isValid(final Byte value, final ConstraintValidatorContext context) {
        if (value == null || value == 0) {
            return false;
        }
        return Role.isValid(value);
    }

}
