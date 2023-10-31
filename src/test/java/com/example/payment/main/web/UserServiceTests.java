package com.example.payment.main.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;
import com.example.payment.utils.IdUtils;

@SpringBootTest
class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    void testCreateHappy() {
        final long id = IdUtils.idLong();
        final String username = "testCreateHappy-" + id;
        User user = new User(id, username, username + " Lastname", "pass-" + id, Role.ADMINISTRATOR.getValue(), true);
        user = this.userService.create(user);
        Optional<User> optUser = this.userService.findById(id);
        assertTrue(optUser.isPresent(), "findById failed");
        assertThat(optUser.get().getUsername()).isEqualTo(username);

        optUser = this.userService.findByUsername(username);
        assertTrue(optUser.isPresent(), "findByUsername failed");
        assertThat(optUser.get().getUsername()).isEqualTo(username);

        this.userService.deleteById(id);
        optUser = this.userService.findById(id);
        assertFalse(optUser.isPresent(), "Delete failed");
    }

    @Test
    void testCreateNegativeRole() {
        final long id = IdUtils.idLong();
        final String username = "testCreateNegativeRole-" + id;
        final User user = new User(id, username, username + " Lastname", "pass-" + id, 0, true);
        final Exception exception = assertThrows(Exception.class, () -> userService.create(user));
        assertNotNull(exception);
        assertEquals(TransactionSystemException.class, exception.getClass());
    }

}
