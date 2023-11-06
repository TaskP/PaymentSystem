package com.example.payment.merchant.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.payment.merchant.factory.TransactionFactory;
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
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(TransactionService.class);

    /**
     * Autowired TransactionRepository.
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * TransactionFactory.
     */
    @Autowired
    private TransactionFactory transactionFactory;

    /**
     * List all Transactions.
     *
     * @return List<Transaction> with all Transactions
     */
    public Page<Transaction> findAll(final Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public Page<Transaction> findAll(final long merchantId, final Pageable pageable) {
        return transactionRepository.findByMerchantId(merchantId, pageable);
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
                throw new EntityExistsException("Transaction exists! UUID:" + transaction.getUuid());
            }
        }
        transactionFactory.setTransactionIdIfNeeded(transaction);
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

    @Scheduled(initialDelay = 30, fixedDelay = 900, timeUnit = TimeUnit.SECONDS)
    public void cleanTransactions() {
        try {
            transactionRepository.deleteByEpochLessThanEqual(System.currentTimeMillis() - 3600_000);
        } catch (final Throwable t) {
            LOG.warn("CleanTransactions Error.", t);
        }
    }
}
