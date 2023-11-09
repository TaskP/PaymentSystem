package com.example.payment.iam.controller;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.payment.common.controller.CommonControllerUI;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.model.UserDetailsImpl;

public class CommonIamControllerUI extends CommonControllerUI {

    /**
     * Validate UserDetails. If not valid return error.
     *
     * @param operation
     * @param userDetails
     * @return User
     */
    protected Object getUser(final String operation, final UserDetails userDetails) {
        if (userDetails == null) {
            if (getLog() != null) {
                getLog().warn("Operation:" + operation + " - missing UserDetails");
            }
            return error(1, "Operation:" + operation + " - missing UserDetails", userDetails);
        }
        if (!(userDetails instanceof UserDetailsImpl)) {
            if (getLog() != null) {
                getLog().warn("Operation:" + operation + " - wrong UserDetails");
            }
            return error(3, "Operation:" + operation + " -  wrong UserDetails", userDetails);
        }
        return ((UserDetailsImpl) userDetails).getUser();
    }

    /**
     * Validate UserDetails. If not valid or not Administrator return error.
     *
     * @param operation
     * @param userDetails
     * @return User if Role is Administrator returns otherwise error
     */
    protected Object getAdministrator(final String operation, final UserDetails userDetails) {
        final Object user = getUser(operation, userDetails);
        if (Role.ADMINISTRATOR.getValue() == ((User) user).getRoleValue()) {
            return user;
        }
        if (getLog() != null) {
            getLog().warn("Operation:" + operation + " -User does not have Role Administrator. UserId:" + ((User) user).getId());
        }
        return error(5, "Operation:" + operation + " - User does not have Role Administrator. UserId:" + ((User) user).getId(), userDetails);
    }

}
