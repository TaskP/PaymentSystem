package com.example.payment.iam.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

/**
 * UserService test cases. Happy path.
 */
@SpringBootTest(classes = com.example.payment.app.main.AppWeb.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestUserServiceHappy {

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
        final long runId = IdUtils.idLong();
        final String clazzName = getClass().getSimpleName();
        final String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        final String fullName = methodName + "-" + runId + "@" + clazzName + ".test";

        final long id = runId;
        final String username = (clazzName + "-" + runId).toLowerCase();
        User user = userFactory.getUser(id, username, fullName, "pass-" + id, Role.ADMINISTRATOR, true);
        user = this.userService.create(user);
        Optional<User> optUser = this.userService.findById(id);
        assertTrue(optUser.isPresent(), "findById failed #1");
        assertTrue(username.equalsIgnoreCase(optUser.get().getUsername()), "findById failed #2");

        optUser = this.userService.findByUsername(username);
        assertTrue(optUser.isPresent(), "findByUsername failed #1");
        assertTrue(optUser.get().getId() == id, "findByUsername failed #2");

        this.userService.deleteById(id);
        optUser = this.userService.findById(id);
        assertFalse(optUser.isPresent(), "Delete failed");
    }
}
