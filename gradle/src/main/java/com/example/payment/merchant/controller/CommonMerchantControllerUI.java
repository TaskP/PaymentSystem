package com.example.payment.merchant.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import com.example.payment.iam.controller.CommonIamControllerUI;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.iam.model.UserDetailsImpl;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.service.MerchantService;

public class CommonMerchantControllerUI extends CommonIamControllerUI {

    /**
     * MerchantService.
     */
    @Autowired
    private MerchantService merchantService;

    protected MerchantService getMerchantService() {
        return merchantService;
    }

    /**
     * Validate UserDetails. If not valid return error.
     *
     * @param caller
     * @param userDetails
     * @return error if not valid, if User Role is Administrator returns User.
     *         Otherwise tries to find Merchant by user id. If not fount return
     *         error.
     */

    protected Object getMerchantOrUser(final String caller, final UserDetails userDetails) {
        if (userDetails == null) {
            if (getLog() != null) {
                getLog().warn(caller + " missing UserDetails");
            }
            return error(1, caller + " missing UserDetails", userDetails);
        }
        if (!(userDetails instanceof UserDetailsImpl)) {
            if (getLog() != null) {
                getLog().warn(caller + " wrong UserDetails");
            }
            return error(3, caller + " wrong UserDetails", userDetails);
        }
        final Object user = getUser(caller, userDetails);
        if (user instanceof ModelAndView) {
            return user;
        }
        if (Role.ADMINISTRATOR.getValue() == ((User) user).getRoleValue()) {
            return user;
        }
        if (Role.MERCHANT.getValue() != ((User) user).getRoleValue()) {
            if (getLog() != null) {
                getLog().warn(caller + " User does not have Role Merchant. UserId:" + ((User) user).getId());
            }
            return error(5, caller + " User does not have Role Merchant. UserId:" + ((User) user).getId(), userDetails);
        }
        Optional<Merchant> ret = null;
        try {
            ret = getMerchantService().findByUserIdUnformatted(((User) user).getId());
        } catch (final Exception e) {
            if (getLog() != null) {
                getLog().warn(caller + " Unable to find merchant by UserId. UserId:" + ((User) user).getId(), e);
            }
            return error(7, caller + " Unable to find merchant by UserId. UserId:" + ((User) user).getId(), userDetails);
        }
        if (ret == null || !ret.isPresent()) {
            if (getLog() != null) {
                getLog().warn(caller + " Merchant not found by UserId. UserId:" + ((User) user).getId());
            }
            return error(9, caller + " Merchant not found by UserId. UserId:" + ((User) user).getId(), userDetails);
        }
        if (!ret.get().getStatus()) {
            if (getLog() != null) {
                getLog().warn(caller + " Merchant status is not active. UserId:" + ((User) user).getId() + " MerchantId:" + ret.get().getId());
            }
            return error(9, caller + " Merchant status is not active. UserId:" + ((User) user).getId() + " MerchantId:" + ret.get().getId(), userDetails);
        }
        return ret.get();
    }

}
