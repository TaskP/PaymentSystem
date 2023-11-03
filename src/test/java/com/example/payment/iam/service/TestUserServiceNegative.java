package com.example.payment.iam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import com.example.payment.common.IdUtils;
import com.example.payment.iam.model.User;

/**
 * UserService test cases. Negative scenarios.
 */
@SpringBootTest(classes = com.example.payment.app.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestUserServiceNegative {

    /**
     * UserService under test.
     */
    @Autowired
    private UserService userService;

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
        final User user = new User(id, username, email, "pass-" + id, 0, true);
        final Exception exception = assertThrows(Exception.class, () -> userService.create(user));
        assertNotNull(exception);
        assertEquals(TransactionSystemException.class, exception.getClass());
    }

}