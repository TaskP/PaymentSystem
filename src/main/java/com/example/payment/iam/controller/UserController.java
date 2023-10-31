package com.example.payment.iam.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;

/**
 * RESTful controller responsible for managing user-related operations through
 * HTTP endpoints in the application's REST API.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Active/Current UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * @return collection of all entities from UserService
     */
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    /**
     * Gets/finds user by user id. Method: HTTP GET.
     *
     * @param id
     * @return Optional<User>
     */
    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable final Long id) {
        return userService.findById(id);
    }

    /**
     * Creates a user. Method: HTTP POST. On success returns an HTTP 201 (Created)
     * status code.
     *
     * @param user
     * @return newly created User
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public User create(@RequestBody final User user) {
        return userService.create(user);
    }

    /**
     * Updates a user. Method: HTTP POST. On success returns an HTTP 201 (Created)
     * status code.
     *
     * @param user
     * @return update User
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PutMapping
    public User update(@RequestBody final User user) throws Exception {
        return userService.update(user);
    }

    /**
     * Deletes a user by user id. Method: HTTP DELETE. On success returns an HTTP
     * 204 (NO CONTENT) status code.
     *
     * @param id
     */
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        userService.deleteById(id);
    }

    /**
     * Gets/finds user by user username. Method: HTTP GET.
     *
     * @param username
     * @return Optional<User>
     */
    @GetMapping("/find/username/{username}")
    public Optional<User> findByUsername(@PathVariable final String username) {
        return userService.findByUsername(username);
    }
}
