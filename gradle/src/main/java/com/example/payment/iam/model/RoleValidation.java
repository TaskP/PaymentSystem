package com.example.payment.iam.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Role validation annotation.
 */
@Constraint(validatedBy = { RoleValidator.class })
@Target(value = { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleValidation {

    /**
     *
     * @return Message that will be set in Validation exception if Role is not valid
     */
    String message() default "Invalid Role";

    /**
     * Constraints may be added to one or more groups. Constraint groups are used to
     * create subsets of constraints so that only certain constraints will be
     * validated for a particular object. By default, all constraints are included
     * in the Default constraint group.
     *
     * @return constraint groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload type that can be attached to a constraint declaration.
     *
     * @return Payload types
     */
    Class<? extends Payload>[] payload() default {};

}
