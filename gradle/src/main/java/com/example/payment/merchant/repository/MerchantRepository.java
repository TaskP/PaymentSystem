package com.example.payment.merchant.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.merchant.model.Merchant;

/**
 * The MerchantRepository interface extends the Spring Data JPA JpaRepository,
 * providing related data access methods. Inherits CRUD (Create, Read, Update,
 * Delete) operations for Merchant entity
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Transactional(readOnly = true)
    Optional<Merchant> findByName(String name);

    @Transactional(readOnly = true)
    Optional<Merchant> findByUsersId(long userId);

    @Query("SELECT merchant FROM Merchant merchant WHERE merchant.name LIKE :name%")
    @Transactional(readOnly = true)
    Page<Merchant> findByName(String name, Pageable pageable);

}
