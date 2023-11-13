package com.example.payment.iam.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import com.example.payment.common.controller.CommonControllerRest;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.model.UserDetailsImpl;

public class CommonIamControllerREST extends CommonControllerRest {

    /**
     * Validate UserDetails. If not valid return error.
     *
     * @param operation
     * @param userDetails
     * @return User
     */
    protected User getUser(final String operation, final UserDetails userDetails) throws ResponseStatusException {
        if (userDetails == null) {
            if (getLog() != null) {
                getLog().warn("Operation:" + operation + " - missing UserDetails");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!(userDetails instanceof UserDetailsImpl)) {
            if (getLog() != null) {
                getLog().warn("Operation:" + operation + " - wrong UserDetails");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ((UserDetailsImpl) userDetails).getUser();
    }

    /**
     *
     * @param operation
     * @param userDetails
     * @return
     * @throws ResponseStatusException
     */
    protected User getAdministrator(final String operation, final UserDetails userDetails) throws ResponseStatusException {
        if (userDetails == null) {
            if (getLog() != null) {
                getLog().warn("Operation:" + operation + " - missing UserDetails");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!(userDetails instanceof UserDetailsImpl)) {
            if (getLog() != null) {
                getLog().warn("Operation:" + operation + " - wrong UserDetails");
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final User user = ((UserDetailsImpl) userDetails).getUser();
        if (Role.ADMINISTRATOR.getValue() == user.getRoleValue()) {
            return user;
        }
        if (getLog() != null) {
            getLog().warn("Operation:" + operation + " - User does not have Role Administrator. UserId:" + user.getId());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

}
