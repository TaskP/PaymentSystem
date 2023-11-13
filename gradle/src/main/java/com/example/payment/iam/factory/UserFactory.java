package com.example.payment.iam.factory;

import com.example.payment.common.utils.IdUtils;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

import jakarta.validation.ValidationException;

/**
 * The UserFactory interface.
 */
public interface UserFactory {

    default long getId() {
        return IdUtils.idLong();
    }

    ValidationException getValidationException(User user);

    default boolean isValid(final User user) {
        return getValidationException(user) == null;
    }

    default void validate(final User user) throws ValidationException {
        final ValidationException valEx = getValidationException(user);
        if (valEx != null) {
            throw valEx;
        }
    }

    default User getUser() {
        return new User();
    }

    default User getUserWithId() {
        return getUser().setId(getId());
    }

    default User setUserIdIfNeeded(final User user) {
        if (user != null && user.getId() == 0) {
            return user.setId(getId());
        }
        return user;
    }

    default User setUserId(final User user) {
        if (user == null) {
            return null;
        }
        return user.setId(getId());
    }

    default String encodePassword(final String password) {
        return password;
    }

    /**
     * Builds new User with all fields. Throws exception if id is zero, username is
     * null or empty or role is null.
     *
     * @param id
     * @param username
     * @param fullName
     * @param password
     * @param role
     * @param status
     * @return User
     * @throws ValidationException
     */
    User getUser(long id, String username, String fullName, String password, Role role, boolean status) throws ValidationException;

    /**
     * Builds new user with mandatory fields. Throws exception if id is zero,
     * username is null or empty or role is null.
     *
     * @param id
     * @param username
     * @param password
     * @param role
     * @return User
     * @throws ValidationException
     */
    default User getUser(final long id, final String username, final String password, final Role role) throws ValidationException {
        return getUser(id, username, null, password, role, true);
    }

    /**
     * Builds new user with mandatory fields. Throws exception if username is null
     * or empty or role is null.
     *
     * @param username
     * @param password
     * @param role
     * @return User
     * @throws ValidationException
     */
    default User getUser(final String username, final String password, final Role role) throws ValidationException {
        return getUser(username, null, password, role);
    }

    default User getUser(final String username, final String fullName, final String password, final Role role) throws ValidationException {
        return getUser(getId(), username, fullName, password, role, true);
    }

}
