package com.example.payment.iam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.payment.iam.model.User;
import com.example.payment.iam.repository.UserRepository;

import jakarta.validation.Valid;

/**
 * Provides a CRUD services and operations related to User.
 */
@Service
public class UserService {

    /**
     * Autowired UserRepository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Autowired PasswordEncoder.
     */
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    /**
     * List all users.
     *
     * @return List with all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Get user by user id.
     *
     * @param id
     * @return Optional<User>
     */
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id);
    }

    /**
     * Persists a user. If PasswordEncoder is set then it is used to hash password
     *
     * @param user
     * @return persisted user
     */
    public User create(@Valid final User user) {
        if (passwordEncoder != null && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    /**
     * Updates a user. If there is a difference between current and new passwords
     * and PasswordEncoder is set then new password is hashed
     *
     * @param user
     * @return persisted user
     * @throws Exception
     */
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

    /**
     * Deletes user by user id.
     *
     * @param id
     */
    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Get user by username.
     *
     * @param username
     * @return Optional<User>
     */
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }
}
