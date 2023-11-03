package com.example.payment.merchant.factory;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.payment.merchant.model.Merchant;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * The UserFactory interface implementation.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MerchantFactoryImpl implements MerchantFactory {

    /**
     * Validator.
     */
    @Autowired
    private Validator validator;

    /**
     * Builds new Merchant with all fields. Throws exception if id is zero, name is
     * null or empty or email is not valid.
     *
     * @param id
     * @param name
     * @param description
     * @param email
     * @param status
     * @return Merchant
     * @throws IllegalArgumentException
     */
    @Override
    public Merchant getMerchant(final long id, final String name, final String description, final String email, final boolean status)
            throws IllegalArgumentException {
        if (id == 0) {
            throw new IllegalArgumentException("Invalid Id");
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid Name");
        }
        final Merchant ret = new Merchant(id, name, description, email, status);
        final Set<ConstraintViolation<Merchant>> valResult = validator.validate(ret);
        if (valResult != null && valResult.size() != 0) {
            throw new IllegalArgumentException("Validation failed! Error:" + valResult);
        }
        return ret;
    }

}
