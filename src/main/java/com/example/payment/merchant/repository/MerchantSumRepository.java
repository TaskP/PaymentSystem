package com.example.payment.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.merchant.model.MerchantSum;

/**
 * MerchantSumRepository.
 */
@Repository
public interface MerchantSumRepository extends JpaRepository<MerchantSum, Long> {

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying
    @Query("update MerchantSum set totalTransactionSum = totalTransactionSum +:amount where merchantId=:id")
    void addAmount(long id, double amount);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying
    @Query("update MerchantSum set totalTransactionSum = totalTransactionSum - :amount where merchantId = :id")
    void subtrackAmount(long id, double amount);
}
