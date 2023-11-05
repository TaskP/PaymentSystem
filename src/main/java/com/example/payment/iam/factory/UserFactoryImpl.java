package com.example.payment.iam.factory;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
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

    @Override
    public ValidationException validate(final User user) {
        if (user == null) {
            return new ValidationException("User is null");
        }
        if (user.getId() == 0) {
            return new ValidationException("Invalid Id");
        }
        if (user.getUsername() == null || user.getUsername().length() == 0) {
            return new ValidationException("Invalid Username");
        }
        if (user.getRole() == 0) {
            return new ValidationException("Invalid Role");
        }
        final Set<ConstraintViolation<User>> valResult = validator.validate(user);
        if (valResult != null && valResult.size() != 0) {
            return new ValidationException("Validation failed! Error:" + valResult);
        }
        return null;
    }

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
            throws ValidationException {
        final User ret = new User().setId(id).setUsername(username).setFullName(fullName).setPassword(password).setRole(role).setStatus(status);
        final ValidationException ex = validate(ret);
        if (ex != null) {
            throw ex;
        }
        return ret;
    }

}
