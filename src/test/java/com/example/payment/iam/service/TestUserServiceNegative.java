package com.example.payment.iam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

import jakarta.validation.ValidationException;

/**
 * UserService test cases. Negative scenarios.
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestUserServiceNegative {

    /**
     * UserService under test.
     */
    @Autowired
    private UserService userService;

    /**
     * UserFactory.
     */
    @Autowired
    private UserFactory userFactory;

    /**
     * Test Role Validation.
     *
     * Creates a User with invalid role. Role validator throws Exception.
     *
     * This test case does not use UserFactory since it checks for Role validity and
     * it will not test persistence validation.
     */
    @Test
    void testInvalidRole() {
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String email = methodName + "-" + runId + "@" + clazzName + ".test";

        final long id = runId;
        final String username = clazzName + "-" + runId;
        final User user = userFactory.getUser(id, username, email, "pass-" + id, Role.ADMINISTRATOR, true);
        user.setRoleValue((byte) System.currentTimeMillis()); // Setting invalid role
        final Exception exception = assertThrows(Exception.class, () -> userService.create(user));
        assertNotNull(exception);
        assertEquals(ValidationException.class, exception.getClass());
    }

}
