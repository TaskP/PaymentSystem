package com.example.payment.merchant.factory;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.payment.iam.model.User;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.MerchantUser;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * The MerchantUserFactoryImpl interface implementation.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MerchantUserFactoryImpl implements MerchantUserFactory {

    /**
     * Validator.
     */
    @Autowired
    private Validator validator;

    /**
     * Builds new MerchantUser with all fields. Throws exception if merchant or user
     * is null or not valid.
     *
     * @param merchant
     * @param user
     * @return Merchant
     * @throws IllegalArgumentException
     */
    @Override
    public MerchantUser getMerchant(final Merchant merchant, final User user) throws IllegalArgumentException {
        if (merchant == null) {
            throw new IllegalArgumentException("Invalid Merchant");
        }
        if (user == null) {
            throw new IllegalArgumentException("Invalid User");
        }
        final MerchantUser ret = new MerchantUser(merchant, user);
        final Set<ConstraintViolation<MerchantUser>> valResult = validator.validate(ret);
        if (valResult != null && valResult.size() != 0) {
            throw new IllegalArgumentException("Validation failed! Error:" + valResult);
        }
        return ret;
    }

}
