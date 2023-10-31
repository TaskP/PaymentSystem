package com.example.payment.iam.model;

import com.example.payment.utils.BitUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<RoleValidation, Long> {

    @Override
    public boolean isValid(final Long value, final ConstraintValidatorContext context) {
        if (value == null || value == 0) {
            return false;
        }
        for (int i = 0; i < 64; i++) {
            if (BitUtils.isSet(i, value)) {
                if (!Role.isValid((byte) i)) {
                    return false;
                }
            }
        }
        return true;
    }

}
