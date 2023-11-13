package com.example.payment.iam.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.iam.TestIamBase;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * User validation test cases.
 *
 * ./gradlew test --tests "TestUserModelValidator"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestUserModelValidator extends TestIamBase {

    /**
     * Validate User Role via Local Validator.
     */
    @Test
    void testUserLocalValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final Validator validator = getValidator();
        final User user = getUserAdministrator(methodName);
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertThat(constraintViolations).hasSize(0);
        user.setRole(-1);
        constraintViolations = validator.validate(user);
        assertThat(constraintViolations).hasSize(1);
    }

    /**
     * Validate User Role via UserFactory Validate.
     */
    @Test
    void testUserFactoryValidator() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = getUserAdministrator(methodName);
        assertDoesNotThrow(() -> getUserFactory().validate(user));

        user.setRole(-1);
        final Exception exception = assertThrows(Exception.class, () -> getUserFactory().validate(user));
        assertNotNull(exception);
    }

}
