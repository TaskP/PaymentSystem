package com.example.payment.merchant.model;

import java.io.Serializable;

import com.example.payment.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Merchant-User data model.
 */
@Entity
@Table(name = "merchant_sum")
public final class MerchantSum implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Merchant ID.
     */
    @Id
    @Column(name = "merchant_id")
    private long merchantId;

    /**
     * Total transaction sum.
     */
    @Column(name = "total_transaction_sum")
    @JsonSerialize(using = JsonUtils.DoublelJsonSerializer.class)
    private double totalTransactionSum;

    /**
     * Default no-arg constructor.
     */
    public MerchantSum() {
        super();
    }

    public MerchantSum(final long merchantId) {
        this(merchantId, 0D);
    }

    public MerchantSum(final long merchantId, final double totalTransactionSumIn) {
        this();
        this.merchantId = merchantId;
        this.totalTransactionSum = totalTransactionSumIn;
    }

    /**
     * @return merchantId
     */
    public long getMerchantId() {
        return merchantId;
    }

    /**
     * @param merchantIdIn
     */
    public void setMerchantId(final long merchantIdIn) {
        this.merchantId = merchantIdIn;
    }

    /**
     * @return totalTransactionSum
     */
    public double getTotalTransactionSum() {
        return totalTransactionSum;
    }

    /**
     * @param totalTransactionSumIn
     */
    public void setTotalTransactionSum(final double totalTransactionSumIn) {
        this.totalTransactionSum = totalTransactionSumIn;
    }

    @Override
    public String toString() {
        return "merchantId=" + merchantId + ", totalTransactionSum=" + totalTransactionSum + "]";
    }

}
