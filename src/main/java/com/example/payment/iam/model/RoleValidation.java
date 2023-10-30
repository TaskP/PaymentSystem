package com.example.payment.iam.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Role validation annotation
 */
@Constraint(validatedBy = { RoleValidator.class })
@Target(value = { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleValidation {

    public boolean allowsNull() default false;

    public String message() default "Invalid Role";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
