package com.example.payment.merchant.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.payment.merchant.factory.TransactionFactory;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.model.TransactionAuthorize;
import com.example.payment.merchant.model.TransactionCharge;
import com.example.payment.merchant.model.TransactionRefund;
import com.example.payment.merchant.model.TransactionReversal;
import com.example.payment.merchant.model.TransactionStatus;
import com.example.payment.merchant.repository.TransactionAuthorizeRepository;
import com.example.payment.merchant.repository.TransactionRepository;
import com.example.payment.merchant.repository.TransactionReversalRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;

/**
 * Provides a CRUD services and operations related to Transaction. Implements
 * Transaction Business Logic:
 *
 * 1. Orders for Tangible Products (Shippable Goods) 1.1 - Authorize
 * Transaction: Hold customer's amount when the order is created. Transaction
 * status is pending. 1.2 - Charge Transaction: Successfully or partially
 * process the order and charge the full or partial amount of the held amount.
 * Reference transaction is in '1.1'. Change the status of the referenced
 * transaction to 'Approved' . 1.3 - Refund Transaction: Customer returns the
 * order, resulting in a refund of the amount collected in the Charge
 * transaction. Reference transaction is in '1.2'. Change the status of the
 * referenced transaction to 'Refunded' . 1.4 - Reversal Transaction: Unable to
 * process the order, resulting in the release of the held customer amount.
 * Reference transaction is in '1.1'. Change the status of the referenced
 * transaction to 'Reversed' .
 *
 * 2. Orders for Non-Tangible Products (e.g., Services) 2.1 - Charge
 * Transaction: Charge the customer with the full amount. 2.2 - Refund
 * Transaction: Customer cancels the order, resulting in a refund of the amount
 * collected in the Charge transaction. Reference transaction is in '2.1'.
 * Change the status of the referenced transaction to 'Refunded' .
 *
 *
 * Distinction between the two scenarios is that the Charge Transaction either
 * has or does not have a reference to the Authorize Transaction.
 *
 * Transactions in status pending older than 1 week are eligible for deletion
 *
 */
@Service
public class TransactionService {

    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(TransactionService.class);

    /**
     * Autowired TransactionRepository.
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Autowired TransactionAuthorizeRepository.
     */
    @Autowired
    private TransactionAuthorizeRepository transactionAuthorizeRepository;

    /**
     * Autowired TransactionReversalRepository.
     */
    @Autowired
    private TransactionReversalRepository transactionReversalRepository;

    /**
     * TransactionFactory.
     */
    @Autowired
    private TransactionFactory transactionFactory;

    /**
     * EntityManagerFactory.
     */
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * List all Transactions.
     *
     * @return List<Transaction> with all Transactions
     */
    public Page<Transaction> findAll(final Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    /**
     * List all Transactions for a MerchantId.
     *
     * @param merchantId
     * @param pageable
     * @return
     */
    public Page<Transaction> findAll(final long merchantId, final Pageable pageable) {
        return transactionRepository.findByMerchantId(merchantId, pageable);
    }

    /**
     * Count transactions per MerchantId.
     *
     * @param merchantId
     * @return
     */
    public long count(final long merchantId) {
        return transactionRepository.countByMerchantId(merchantId);
    }

    /**
     * Get Transaction by Transaction ID.
     *
     * @param id
     * @return Optional<Transaction>
     */
    public Optional<Transaction> findById(final UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return transactionRepository.findById(id);
    }

    /**
     * Create TransactionAuthorize.
     *
     * @param transactionAuthz
     * @return persisted Transaction
     */
    protected TransactionAuthorize createTransactionAuthorize(final TransactionAuthorize transactionAuthz) {
        transactionAuthz.setReferenceTransaction(null);
        transactionAuthz.setStatus(TransactionStatus.PENDING);
        transactionFactory.validate(transactionAuthz);
        return transactionRepository.save(transactionAuthz);
    }

    /**
     * Create TransactionCharge.
     *
     * @param transactionAuthz
     * @return persisted Transaction
     */
    protected TransactionCharge createTransactionCharge(final TransactionCharge transactionCharge) {
        transactionCharge.setStatus(TransactionStatus.APPROVED);
        transactionFactory.validate(transactionCharge);
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            txn = entityManager.getTransaction();
            txn.begin();
            if (transactionCharge.getReferenceId() != null) {
                final TransactionAuthorize transactionAuthz = entityManager.find(TransactionAuthorize.class, transactionCharge.getReferenceId());
                if (transactionAuthz == null) {
                    throw new DataIntegrityViolationException("Transaction reference not found by ReferenceId:" + transactionCharge.getReferenceId());
                }
                if (transactionCharge.getMerchantId() != transactionAuthz.getMerchantId()) {
                    throw new ValidationException("Merchant differ");
                }
                if (!TransactionStatus.PENDING.equals(transactionAuthz.getStatusType())) {
                    throw new DataIntegrityViolationException("Transaction reference status is not Pending! ReferenceId:" + transactionCharge.getReferenceId());
                }
                if (transactionCharge.getAmount() > transactionAuthz.getAmount()) {
                    throw new DataIntegrityViolationException("Transaction reference amount is smaller! ReferenceId:" + transactionCharge.getReferenceId());
                }
                transactionAuthz.setStatus(TransactionStatus.APPROVED);
                entityManager.merge(transactionAuthz);
                transactionCharge.setReferenceTransaction(transactionAuthz);
            }
            entityManager.persist(transactionCharge);
         // @formatter:off
            final int count = entityManager.createQuery("update MerchantSum set totalTransactionSum = totalTransactionSum +:amount where merchantId=:id")
                    .setParameter("amount", transactionCharge.getAmount())
                    .setParameter("id", transactionCharge.getMerchantId())
                    .executeUpdate();
         // @formatter:on
            if (count != 1) {
                throw new DataIntegrityViolationException("MerchantSum update failed. Expecting 1 but received:" + count);
            }
            txn.commit();
            return transactionCharge;
        } catch (final Exception e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    protected TransactionRefund createTransactionRefund(final TransactionRefund transactionRefund) {
        transactionRefund.setStatus(TransactionStatus.REFUNDED);
        transactionFactory.validate(transactionRefund);
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            txn = entityManager.getTransaction();
            txn.begin();
            final TransactionCharge transactionCharge = entityManager.find(TransactionCharge.class, transactionRefund.getReferenceId());
            if (transactionCharge == null) {
                throw new DataIntegrityViolationException("Transaction reference not found by ReferenceId:" + transactionRefund.getReferenceId());
            }
            if (transactionRefund.getMerchantId() != transactionCharge.getMerchantId()) {
                throw new ValidationException("Merchant differ");
            }
            if (!TransactionStatus.APPROVED.equals(transactionCharge.getStatusType())) {
                throw new DataIntegrityViolationException("Transaction reference status is not Approved. ReferenceId:" + transactionRefund.getReferenceId());
            }
            if (transactionRefund.getAmount() > transactionCharge.getAmount()) {
                throw new DataIntegrityViolationException("Transaction reference amount is smaller. ReferenceId:" + transactionRefund.getReferenceId());
            }

            transactionCharge.setStatus(TransactionStatus.REFUNDED);
            entityManager.merge(transactionCharge);
            transactionRefund.setReferenceTransaction(transactionCharge);
            entityManager.persist(transactionRefund);
            //@formatter:off
            final int count = entityManager.createQuery("update MerchantSum set totalTransactionSum = totalTransactionSum -:amount where merchantId=:id")
                    .setParameter("amount", transactionRefund.getAmount())
                    .setParameter("id", transactionRefund.getMerchantId())
                    .executeUpdate();
           //@formatter:on
            if (count != 1) {
                throw new DataIntegrityViolationException("MerchantSum update failed. Expecting 1 but received:" + count);
            }
            txn.commit();
            return transactionRefund;
        } catch (final Exception e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Transactional
    protected TransactionReversal createTransactionReversal(final TransactionReversal transactionReversal) {
        transactionReversal.setStatus(TransactionStatus.REVERSED);
        transactionFactory.validate(transactionReversal);
        final Optional<TransactionAuthorize> transactionAuthz = transactionAuthorizeRepository.findById(transactionReversal.getReferenceId());
        if (!transactionAuthz.isPresent()) {
            throw new DataIntegrityViolationException("Transaction reference not found by ReferenceId:" + transactionReversal.getReferenceId());
        }
        if (transactionReversal.getMerchantId() != transactionAuthz.get().getMerchantId()) {
            throw new ValidationException("Merchant differ");
        }
        if (!TransactionStatus.PENDING.equals(transactionAuthz.get().getStatusType())) {
            throw new DataIntegrityViolationException("Transaction reference status is not Pending. ReferenceId:" + transactionReversal.getReferenceId());
        }

        transactionAuthz.get().setStatus(TransactionStatus.REVERSED);
        transactionReversal.setReferenceTransaction(transactionAuthz.get());
        final List<Transaction> persistList = new LinkedList<Transaction>();
        persistList.clear();
        persistList.add(transactionReversal);
        persistList.add(transactionAuthz.get());
        transactionRepository.saveAll(persistList);
        return transactionReversal;
    }

    /**
     * Create a Transaction.
     *
     * @param transaction
     * @return persisted Transaction
     */
    public Transaction create(@NotNull final Transaction transaction) {
        if (transaction.getUuid() != null) {
            final Optional<Transaction> ex = findById(transaction.getUuid());
            if (ex.isPresent()) {
                throw new EntityExistsException("Duplicate Transaction with UUID:" + transaction.getUuid());
            }
        }
        transactionFactory.setTransactionIdIfNeeded(transaction);
        if (transaction instanceof TransactionAuthorize) {
            return createTransactionAuthorize((TransactionAuthorize) transaction);
        }
        if (transaction instanceof TransactionCharge) {
            return createTransactionCharge((TransactionCharge) transaction);
        }
        if (transaction instanceof TransactionRefund) {
            return createTransactionRefund((TransactionRefund) transaction);
        }
        if (transaction instanceof TransactionReversal) {
            return createTransactionReversal((TransactionReversal) transaction);
        }
        throw new ValidationException("Transaction type (" + transaction.getType() + ") not supported");
    }

    /**
     * Deletes Transaction by Transaction ID.
     *
     * @param id
     */
    public void deleteById(final UUID id) {
        transactionRepository.deleteById(id);
    }

    public void cleanTransactions() {
        long epoch = System.currentTimeMillis() - (7 * 86400 * 1000);
        transactionAuthorizeRepository.deleteStaleTransactions(epoch, TransactionStatus.PENDING.getStatusId());

        epoch = System.currentTimeMillis() - (31 * 86400 * 1000);
        final List<TransactionReversal> list = transactionReversalRepository.findByEpochLessThanEqual(epoch);
        if (list == null || list.isEmpty()) {
            return;
        }
        final List<Transaction> deleteList = new LinkedList<Transaction>();
        for (final TransactionReversal item : list) {
            if (item.getReferenceTransaction() == null) {
                throw new DataIntegrityViolationException("Transaction reference not found. Uuid:" + item.getUuid());
            }
            if (!TransactionStatus.REVERSED.equals(item.getReferenceTransaction().getStatusType())) {
                throw new DataIntegrityViolationException("Transaction reference status type is not REVERSED. ReferenceId:" + item.getReferenceId());
            }
            deleteList.clear();
            deleteList.add(item);
            deleteList.add(item.getReferenceTransaction());
            transactionRepository.deleteAll(deleteList);
        }

    }
}
