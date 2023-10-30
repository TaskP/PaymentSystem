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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable final Long id) {
        return userService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public User create(@RequestBody final User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        userService.deleteById(id);
    }

    @GetMapping("/find/username/{username}")
    public Optional<User> findByUsername(@PathVariable final String username) {
        return userService.findByUsername(username);
    }

}