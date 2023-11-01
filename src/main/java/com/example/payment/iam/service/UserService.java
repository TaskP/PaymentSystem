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
     * Strip user password.
     *
     * @param user
     * @return user
     */
    protected User formatOut(final User user) {
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    /**
     * Strip user password.
     *
     * @param user optional
     * @return Optional<User>
     */
    protected Optional<User> formatOut(final Optional<User> user) {
        if (user != null && user.isPresent()) {
            formatOut(user.get());
        }
        return user;
    }

    /**
     * Strip user password.
     *
     * @param users list
     * @return List<User>
     */
    protected List<User> formatOut(final List<User> users) {
        if (users != null && !users.isEmpty()) {
            for (final User user : users) {
                formatOut(user);
            }
        }
        return users;
    }

    /**
     * List all users.
     *
     * @return List<User> with all users
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
        return formatOut(userRepository.findById(id));
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
        return formatOut(userRepository.save(user));
    }

    /**
     * Updates a user. If new password is null or empty use current. If there is a
     * difference between current and new passwords and PasswordEncoder is set then
     * new password is hashed
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
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(ex.get().getPassword());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (passwordEncoder != null && !user.getPassword().equals(ex.get().getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        return formatOut(userRepository.save(user));
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
        return formatOut(userRepository.findByUsername(username));
    }
}
