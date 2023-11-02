package com.example.payment.merchant.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.repository.TransactionRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;

/**
 * Provides a CRUD services and operations related to Transaction.
 */
@Service
public class TransactionService {

    /**
     * Autowired TransactionRepository.
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * List all Transactions.
     *
     * @return List<Transaction> with all Transactions
     */
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * Get Transaction by Transaction ID.
     *
     * @param id
     * @return Optional<Transaction>
     */
    public Optional<Transaction> findById(final UUID id) {
        return transactionRepository.findById(id);
    }

    /**
     * Create a Transaction.
     *
     * @param transaction
     * @return persisted Transaction
     */
    public Transaction create(@Valid final Transaction transaction) throws EntityExistsException {
        if (transaction.getUuid() != null) {
            final Optional<Transaction> ex = findById(transaction.getUuid());
            if (ex.isPresent()) {
                throw new EntityExistsException("Transaction exists! Id:" + transaction.getUuid());
            }
        }
        return transactionRepository.save(transaction);
    }

    /**
     * Deletes Transaction by Transaction ID.
     *
     * @param id
     */
    public void deleteById(final UUID id) {
        transactionRepository.deleteById(id);
    }
}
