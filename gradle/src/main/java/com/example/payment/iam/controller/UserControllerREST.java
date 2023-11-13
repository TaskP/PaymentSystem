package com.example.payment.iam.controller;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;

/**
 * RESTful controller responsible for managing user-related operations through
 * HTTP end points.
 */

@RestController
@RequestMapping
public class UserControllerREST extends CommonIamControllerREST {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(UserControllerREST.class);

    /**
     * UserService.
     */
    @Autowired
    private UserService userService;

    @Override
    protected Log getLog() {
        return LOG;
    }

    /**
     * @return collection of all entities from UserService
     */
    @GetMapping(path = { "/api/user" })
    public List<User> findAll(@AuthenticationPrincipal final UserDetails userDetails) {
        getAdministrator("FindAll", userDetails);
        try {
            return userService.findAll();
        } catch (final Exception e) {
            throw error("FindAll failed!", e);
        }
    }

    /**
     * Gets/finds user by user username. Method: HTTP GET. If username is null or
     * empty lists all users.
     *
     * @param username
     * @return Object - Optional<User> or List<User>
     */
    @GetMapping("/api/users")
    public Object findByUsername(@RequestParam(required = false, name = "username") final String username,
            @AuthenticationPrincipal final UserDetails userDetails) {
        getAdministrator("FindByUsername", userDetails);

        try {
            return userService.findByUsername(username);
        } catch (final Exception e) {
            throw error("FindByUsername failed!", e);
        }
    }

    /**
     * Gets/finds user by user id. Method: HTTP GET.
     *
     * @param id
     * @return Optional<User>
     */
    @GetMapping(path = { "/api/user/{id}", "/api/users/{id}" })
    public Optional<User> findById(@PathVariable final long id, @AuthenticationPrincipal final UserDetails userDetails) {
        getAdministrator("GetById", userDetails);
        try {
            return userService.findById(id);
        } catch (final Exception e) {
            throw error("FindById failed!", e);
        }
    }

    /**
     * Creates a user. Method: HTTP POST. On success returns an HTTP 201 (Created)
     * status code. On duplicate user error returns HTTP 409 Conflict
     *
     * @param user
     * @return newly created User
     */
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping(path = { "/api/user", "/api/users" })
    public User create(@RequestBody final User user, @AuthenticationPrincipal final UserDetails userDetails) {
        getAdministrator("Create", userDetails);
        try {
            return userService.create(user);
        } catch (final Exception e) {
            throw error("Create failed!", e);
        }
    }

    /**
     * Updates a user. Method: HTTP POST. On success returns an HTTP 200 (OK) status
     * code. On user not found returns HTTP 404 Not found. On duplicate user error
     * returns HTTP 409 Conflict
     *
     * @param user
     * @return update User
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = { "/api/users/{id}", "/api/users/{id}" })
    public User update(@PathVariable final long id, @RequestBody final User user, @AuthenticationPrincipal final UserDetails userDetails) {
        getAdministrator("Update", userDetails);
        try {
            user.setId(id);
            return userService.update(user);
        } catch (final Exception e) {
            throw error("Update failed!", e);
        }
    }

    /**
     * Deletes a user by user id. Method: DELETE. On success returns an HTTP 204 (NO
     * CONTENT) status code. On user not found returns HTTP 404 Not found. On user
     * in user(constraint violation) returns HTTP 400 Bad request
     *
     * @param id
     */
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping(path = { "/api/user/{id}", "/api/users/{id}" })
    public void deleteById(@PathVariable final long id, @AuthenticationPrincipal final UserDetails userDetails) {
        getAdministrator("Delete", userDetails);
        try {
            userService.deleteById(id);
        } catch (final Exception e) {
            throw error("Delete failed!", e);
        }

    }
}
