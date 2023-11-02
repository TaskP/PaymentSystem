package com.example.payment.merchant.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.payment.merchant.model.Transaction;

/**
 * The TransactionRepository interface extends the Spring Data JPA
 * JpaRepository, providing related data access methods. Inherits CRUD (Create,
 * Read, Update, Delete) operations for Merchant entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
