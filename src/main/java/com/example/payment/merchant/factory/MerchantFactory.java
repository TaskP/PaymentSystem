package com.example.payment.merchant.factory;

import com.example.payment.merchant.model.Merchant;

/**
 * The MerchantFactory interface.
 */
public interface MerchantFactory {

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
    Merchant getMerchant(long id, String name, String description, String email, boolean status) throws IllegalArgumentException;

    /**
     * Builds new merchant with all fields. Throws exception if id is zero, name is
     * null or empty or email is not valid.
     *
     * @param id
     * @param name
     * @param email
     * @return Merchant
     * @throws IllegalArgumentException
     */
    default Merchant getMerchant(final long id, final String name, final String email) throws IllegalArgumentException {
        return getMerchant(id, name, null, email, true);
    }

}
