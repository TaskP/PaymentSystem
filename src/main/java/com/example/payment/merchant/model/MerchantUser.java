package com.example.payment.merchant.model;

import java.io.Serializable;

import com.example.payment.iam.model.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

/**
 * Merchant-User data model.
 */
@Entity
public final class MerchantUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * EmbeddedId MerchantUserKey.
     */
    @EmbeddedId
    private MerchantUserKey id;

    /**
     * Merchant.
     */
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    /**
     * User.
     */
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Default no-arg constructor.
     */
    public MerchantUser() {
        super();
    }

    /**
     * @param merchantIn
     * @param userIn
     */
    public MerchantUser(final Merchant merchantIn, final User userIn) {
        this();
        this.setMerchant(merchantIn);
        this.setUser(userIn);
    }

    /**
     * @return MerchantUserKey
     */
    public MerchantUserKey getId() {
        return id;
    }

    /**
     * @param idIn
     */
    public void setId(final MerchantUserKey idIn) {
        this.id = idIn;
    }

    /**
     * Populates MerchantUserKey with respective ids.
     */
    protected void updateId() {
        synchronized (this) {
            if (id == null) {
                id = new MerchantUserKey();
            }
            final Merchant lMerchant = this.getMerchant();
            final User lUser = this.getUser();
            if (lMerchant != null) {
                id.setMerchantId(lMerchant.getId());
            } else {
                id.setMerchantId(null);
            }
            if (lUser != null) {
                id.setUserId(lUser.getId());
            } else {
                id.setUserId(null);
            }
        }

    }

    /**
     * @return Merchant
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * @param merchantIn
     */
    public void setMerchant(final Merchant merchantIn) {
        this.merchant = merchantIn;
        updateId();
    }

    /**
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * @param userIn
     */
    public void setUser(final User userIn) {
        this.user = userIn;
        updateId();
    }

    @Override
    public String toString() {
        return "MerchantUser [id=" + getId() + ", merchant=" + getMerchant() + ", user=" + getUser() + "]";
    }

}
