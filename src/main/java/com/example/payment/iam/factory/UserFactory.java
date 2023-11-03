package com.example.payment.iam.factory;

import com.example.payment.common.IdUtils;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;

/**
 * The UserFactory interface.
 */
public interface UserFactory {

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
     * @throws IllegalArgumentException
     */
    User getUser(long id, String username, String fullName, String password, Role role, boolean status) throws IllegalArgumentException;

    /**
     * Builds new user with mandatory fields. Throws exception if id is zero,
     * username is null or empty or role is null.
     *
     * @param id
     * @param username
     * @param password
     * @param role
     * @return User
     * @throws IllegalArgumentException
     */
    default User getUser(final long id, final String username, final String password, final Role role) throws IllegalArgumentException {
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
     * @throws IllegalArgumentException
     */
    default User getUser(final String username, final String password, final Role role) throws IllegalArgumentException {
        return getUser(IdUtils.idLong(), username, password, role);
    }

}
