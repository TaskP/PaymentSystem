package com.example.payment.merchant.factory;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.merchant.model.Merchant;

import jakarta.validation.ValidationException;

/**
 * The MerchantFactory interface.
 */
public interface MerchantFactory {

    default long getId() {
        return IdUtils.idLong();
    }

    ValidationException validate(Merchant merchant);

    default boolean isValid(final Merchant merchant) {
        return validate(merchant) == null;
    }

    default Merchant getMerchant() {
        return new Merchant();
    }

    default Merchant getMerchantWithId() {
        return getMerchant().setId(getId());
    }

    default Merchant setMerchantIdIfNeeded(final Merchant merchant) {
        if (merchant != null && merchant.getId() == 0) {
            return merchant.setId(getId());
        }
        return merchant;
    }

    default Merchant setMerchantId(final Merchant merchant) {
        if (merchant == null) {
            return null;
        }
        return merchant.setId(getId());
    }

    /**
     * Builds new Merchant with all fields. Throws exception if id is zero, username
     * is null or empty or email is not valid.
     *
     * @param id
     * @param name
     * @param description
     * @param email
     * @param status
     * @return Merchant
     * @throws IllegalArgumentException
     */
    Merchant getMerchant(long id, String name, String description, String email, boolean status) throws ValidationException;

    default Merchant getMerchant(final long id, final String name, final String email) throws ValidationException {
        return getMerchant(id, name, null, email, true);
    }

    default Merchant getMerchant(final long id, final String name) throws ValidationException {
        return getMerchant(id, name, null);
    }

}
