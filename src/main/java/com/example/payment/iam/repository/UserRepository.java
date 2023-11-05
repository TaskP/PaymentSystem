package com.example.payment.iam.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.iam.model.User;

/**
 * The UserRepository interface extends the Spring Data JPA JpaRepository,
 * providing data access and management methods. Inherits CRUD (Create, Read,
 * Update, Delete) operations for User entity
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

    /**
     * Returns all. Pageable.
     **/
    @Override
    @Query("SELECT user FROM User user")
    @Transactional(readOnly = true)
    Page<User> findAll(Pageable pageable);

    @Query("SELECT user FROM User user WHERE user.username LIKE :username%")
    @Transactional(readOnly = true)
    Page<User> findByUsername(String username, Pageable pageable);

}
