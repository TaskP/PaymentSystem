package com.example.payment.iam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.payment.iam.model.User;
import com.example.payment.iam.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(final Long id) {
        return userRepository.findById(id);
    }

    public User create(@Valid final User user) {
        if (passwordEncoder != null && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User update(@Valid final User user) throws Exception {
        final Optional<User> ex = findById(user.getId());
        if (!ex.isPresent()) {
            throw new Exception("User not found! Id:" + user.getId());
        }
        if (passwordEncoder != null && ex.get().getPassword() != null && !user.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}