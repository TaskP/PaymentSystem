package com.example.payment.merchant.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;

/**
 * Merchant-User data model.
 */
@Embeddable
@Table(name = "merchant_user")
public final class MerchantUserKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Merchant ID.
     */
    @Column(name = "merchant_id")
    private long merchantId;

    /**
     * User ID.
     */
    @Column(name = "user_id")
    private long userId;

    /**
     * Default no-arg constructor.
     */
    public MerchantUserKey() {
        super();
    }

    /**
     * All fields constructor.
     *
     * @param merchantIdIn
     * @param userIdIn
     */
    public MerchantUserKey(final long merchantIdIn, final long userIdIn) {
        this();
        this.merchantId = merchantIdIn;
        this.userId = userIdIn;
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
     * @return userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userIdIn
     */
    public void setUserId(final long userIdIn) {
        this.userId = userIdIn;
    }

    @Override
    public String toString() {
        return "MerchantUserKey [merchantId=" + getMerchantId() + ", userId=" + getUserId() + "]";
    }

}
