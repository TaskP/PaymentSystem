package com.example.payment.iam.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.TestIamBase;
import com.example.payment.iam.model.User;

/**
 * UserService test cases. Negative scenarios.
 *
 * ./gradlew test --tests "TestUserServiceNegative"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestUserServiceNegative extends TestIamBase {

    /**
     * Test duplicate username create.
     *
     * Steps:
     *
     * 1. Create a User
     *
     * 2. Create a User with same username and different id.
     *
     * 3. Exception is thrown
     *
     * 4. Deletes User
     */
    @Test
    void testDuplicateUsername() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = createUser(methodName);
        final User userDuplicate = getUserAdministrator(methodName);
        userDuplicate.setId(IdUtils.idLong() * 123);

        final Exception exception = assertThrows(Exception.class, () -> createUser(userDuplicate));
        assertNotNull(exception);
        deleteUser(user);
    }

}
