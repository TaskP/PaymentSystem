package com.example.payment.merchant.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.merchant.model.TransactionAuthorize;

@Repository
@Transactional
public interface TransactionAuthorizeRepository extends JpaRepository<TransactionAuthorize, UUID> {

    @Modifying
    @Transactional
    @Query("delete from TransactionAuthorize t where t.epoch < :epoch and t.status=:status")
    void deleteStaleTransactions(@Param("epoch") long epoch, @Param("status") byte status);

}
