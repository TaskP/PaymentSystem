package com.example.payment.merchant.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.merchant.model.TransactionReversal;

@Repository
@Transactional
public interface TransactionReversalRepository extends JpaRepository<TransactionReversal, UUID> {

    List<TransactionReversal> findByEpochLessThanEqual(@Param("epoch") long epoch);
}
