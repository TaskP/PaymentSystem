package com.example.payment.merchant.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
    private long id;

    /**
     * Merchant.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    /**
     * Total transaction sum.
     */
    @Column(name = "total_transaction_sum")
    private double totalTransactionSum;

    /**
     * Default no-arg constructor.
     */
    public MerchantSum() {
        super();
    }

    /**
     * MerchantSum.
     *
     * @param merchantId
     * @param totalTransactionSumIn
     */
    public MerchantSum(final long merchantId, final double totalTransactionSumIn) {
        this();
        this.id = merchantId;
        this.totalTransactionSum = totalTransactionSumIn;
    }

    /**
     * @return merchantId
     */
    public long getMerchantId() {
        return id;
    }

    /**
     * @param merchantIdIn
     */
    public void setMerchantId(final long merchantIdIn) {
        this.id = merchantIdIn;
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

}
