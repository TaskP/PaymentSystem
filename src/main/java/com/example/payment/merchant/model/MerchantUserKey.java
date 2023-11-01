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

    // @Column(unique = true)
    // private Long id = IdUtils.idLong();

    /**
     * Merchant ID.
     */
    @Column(name = "merchant_id")
    private Long merchantId;

    /**
     * User ID.
     */
    @Column(name = "user_id")
    private Long userId;

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
    public MerchantUserKey(final Long merchantIdIn, final Long userIdIn) {
        this();
        this.merchantId = merchantIdIn;
        this.userId = userIdIn;
    }

    /**
     * @return merchantId
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * @param merchantIdIn
     */
    public void setMerchantId(final Long merchantIdIn) {
        this.merchantId = merchantIdIn;
    }

    /**
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userIdIn
     */
    public void setUserId(final Long userIdIn) {
        this.userId = userIdIn;
    }

    @Override
    public String toString() {
        return "MerchantUserKey [merchantId=" + getMerchantId() + ", userId=" + getUserId() + "]";
    }

}
