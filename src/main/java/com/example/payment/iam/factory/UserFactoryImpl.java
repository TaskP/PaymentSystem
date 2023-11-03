package com.example.payment.iam.factory;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * The UserFactory interface implementation.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserFactoryImpl implements UserFactory {

    /**
     * Validator.
     */
    @Autowired
    private Validator validator;

    /**
     * Builds new User with all fields. Throws exception if username is null or
     * empty or role is null.
     *
     * @param id
     * @param username
     * @param fullName
     * @param password
     * @param role
     * @param status
     * @return User
     * @throws IllegalArgumentException
     */
    @Override
    public User getUser(final long id, final String username, final String fullName, final String password, final Role role, final boolean status)
            throws IllegalArgumentException {
        if (id == 0) {
            throw new IllegalArgumentException("Invalid Id");
        }
        if (username == null || username.length() == 0) {
            throw new IllegalArgumentException("Invalid Username");
        }
        if (role == null) {
            throw new IllegalArgumentException("Invalid Role");
        }
        final User ret = new User(id, username, fullName, password, role.getValue(), status);

        final Set<ConstraintViolation<User>> valResult = validator.validate(ret);
        if (valResult != null && valResult.size() != 0) {
            throw new IllegalArgumentException("Validation failed! Error:" + valResult);
        }
        return ret;
    }

}
