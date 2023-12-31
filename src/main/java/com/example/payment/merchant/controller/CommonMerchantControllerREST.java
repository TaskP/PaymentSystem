package com.example.payment.merchant.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import com.example.payment.iam.controller.CommonIamControllerREST;
import com.example.payment.iam.model.Role;
import com.example.payment.iam.model.User;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.service.MerchantService;

public class CommonMerchantControllerREST extends CommonIamControllerREST {

    /**
     * MerchantService.
     */
    @Autowired
    private MerchantService merchantService;

    protected MerchantService getMerchantService() {
        return merchantService;
    }

    protected Merchant getMerchant(final String caller, final UserDetails userDetails) throws ResponseStatusException {
        final User user = getUser(caller, userDetails);
        if (Role.MERCHANT.getValue() != user.getRoleValue()) {
            if (getLog() != null) {
                getLog().warn(caller + " User does not have Role Merchant. UserId:" + user.getId());
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Optional<Merchant> ret = null;
        try {
            ret = getMerchantService().findByUserIdUnformatted(user.getId());
        } catch (final Exception e) {
            if (getLog() != null) {
                getLog().warn(caller + " Unable to find merchant by UserId. UserId:" + user.getId(), e);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (ret == null || !ret.isPresent()) {
            if (getLog() != null) {
                getLog().warn(caller + " Merchant not found by UserId. UserId:" + user.getId());
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!ret.get().getStatus()) {
            if (getLog() != null) {
                getLog().warn(caller + " Merchant status is not active. UserId:" + user.getId() + " MerchantId:" + ret.get().getId());
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ret.get();
    }

}
