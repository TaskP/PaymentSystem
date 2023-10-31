package com.example.payment.iam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.payment.iam.model.User;

/**
 * The UserRepository interface extends the Spring Data JPA JpaRepository,
 * providing user-related data access and management methods. Inherits CRUD
 * (Create, Read, Update, Delete) operations for user entities
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Gets/Finds user by username.
     *
     * @param username
     * @return Optional<User>
     */
    Optional<User> findByUsername(String username);

}
