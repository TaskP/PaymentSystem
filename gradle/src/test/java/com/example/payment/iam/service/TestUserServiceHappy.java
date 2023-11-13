package com.example.payment.iam.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.iam.TestIamBase;
import com.example.payment.iam.model.User;

/**
 * UserService test cases. Happy path.
 *
 * ./gradlew test --tests "TestUserServiceHappy"
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestUserServiceHappy extends TestIamBase {

    /**
     * Test create.
     *
     * Steps:
     *
     * 1. Creates a User
     *
     * 2. Fetches User by Id and compares Username
     *
     * 3. Fetches User by Username and compares Id
     *
     * 3. Deletes User
     */
    @Test
    void testCreate() {
        // @formatter:off
        final String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
        // @formatter:on

        final User user = createUser(methodName);
        Optional<User> optUser = getUserService().findById(user.getId());
        assertTrue(optUser.isPresent(), "findById failed #1");
        assertTrue(user.getUsername().equalsIgnoreCase(optUser.get().getUsername()), "findById failed #2");

        optUser = getUserService().findByUsername(user.getUsername());
        assertTrue(optUser.isPresent(), "findByUsername failed #1");
        assertTrue(optUser.get().getId() == user.getId(), "findByUsername failed #2");

        deleteUser(user);
        optUser = getUserService().findById(optUser.get().getId());
        assertFalse(optUser.isPresent(), "Delete failed");
    }
}
