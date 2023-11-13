package com.example.payment.merchant.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.merchant.model.Transaction;

/**
 * The TransactionRepository interface extends the Spring Data JPA
 * JpaRepository, providing related data access methods. Inherits CRUD (Create,
 * Read, Update, Delete) operations for Transaction entity
 */
@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Override
    @Query("SELECT transaction FROM Transaction transaction")
    @Transactional(readOnly = true)
    Page<Transaction> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Page<Transaction> findByMerchantId(long merchantId, Pageable pageable);

    @Transactional(readOnly = true)
    long countByMerchantId(long merchantId);

}
