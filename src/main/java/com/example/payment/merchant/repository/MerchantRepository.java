package com.example.payment.merchant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.payment.merchant.model.Merchant;

/**
 * The MerchantRepository interface extends the Spring Data JPA JpaRepository,
 * providing related data access methods. Inherits CRUD (Create, Read, Update,
 * Delete) operations for Merchant entity
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    /**
     * Gets/Finds Merchant by merchant name.
     *
     * @param name
     * @return Optional<Merchant>
     */
    Optional<Merchant> findByName(String name);

}
