package com.example.payment.merchant.factory;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.payment.merchant.model.Merchant;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
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

    @Override
    public ValidationException getValidationException(final Merchant merchant) {
        if (merchant == null) {
            return new ValidationException("Merchant is null");
        }
        if (merchant.getId() == 0) {
            return new ValidationException("Invalid Id");
        }
        if (merchant.getName() == null || merchant.getName().length() == 0) {
            return new ValidationException("Invalid Name");
        }
        final Set<ConstraintViolation<Merchant>> valResult = validator.validate(merchant);
        if (valResult != null && valResult.size() != 0) {
            return new ValidationException("Validation failed! Error:" + valResult);
        }
        return null;
    }

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
     * @throws ValidationException
     */
    @Override
    public Merchant getMerchant(final long id, final String name, final String description, final String email, final boolean status)
            throws ValidationException {
        if (id == 0) {
            throw new ValidationException("Invalid Id");
        }
        if (name == null || name.length() == 0) {
            throw new ValidationException("Invalid Name");
        }
        final Merchant ret = new Merchant();
        ret.setId(id);
        ret.setName(name);
        ret.setDescription(description);
        ret.setEmail(email);
        ret.setStatus(status);
        final ValidationException ex = getValidationException(ret);
        if (ex != null) {
            throw ex;
        }
        return ret;
    }

}
