package com.example.payment.merchant.factory;

import com.example.payment.iam.model.User;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.MerchantUser;

/**
 * The MerchantUserFactory interface.
 */
public interface MerchantUserFactory {

    /**
     * Builds new MerchantUser with all fields. Throws exception if merchant or user
     * is null or not valid.
     *
     * @param merchant
     * @param user
     * @return Merchant
     * @throws IllegalArgumentException
     */
    MerchantUser getMerchant(Merchant merchant, User user) throws IllegalArgumentException;

}
