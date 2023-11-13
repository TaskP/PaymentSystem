package com.example.payment.common.controller;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ValidationException;

/**
 * Common UI controller.
 */
public class CommonControllerUI {

    /**
     * BuildProperties.
     */
    @Autowired
    private BuildProperties buildProperties;

    protected Log getLog() {
        return null;
    }

    protected String getErrorViewName() {
        return "error";
    }

    protected ModelAndView error(final int errno, final String message, final UserDetails userDetails) {
        if (getLog() != null) {
            getLog().warn("Errno:" + errno + " Error: " + message);
        }

        final ModelAndView mav = new ModelAndView();
        mav.addObject("errno", errno);
        mav.addObject("message", message);
        mav.addObject("applicationVersion", buildProperties.getVersion());
        if (userDetails != null) {
            mav.addObject("Username", userDetails.getUsername());
            final Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            if (authorities != null && authorities.size() > 0) {
                mav.addObject("Role", authorities.iterator().next().getAuthority());
            }
        }
        mav.setViewName(getErrorViewName());
        return mav;
    }

    protected ModelAndView error(final String message, final UserDetails userDetails) {
        if (getLog() != null) {
            getLog().warn("Error: " + message);
        }
        final ModelAndView mav = new ModelAndView();
        mav.addObject("message", message);
        mav.addObject("applicationVersion", buildProperties.getVersion());
        if (userDetails != null) {
            mav.addObject("Username", userDetails.getUsername());
            final Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            if (authorities != null && authorities.size() > 0) {
                mav.addObject("Role", authorities.iterator().next().getAuthority());
            }
        }
        mav.setViewName(getErrorViewName());
        return mav;
    }

    protected ModelAndView error(final String message, final Exception e, final UserDetails userDetails) {
        if (getLog() != null) {
            getLog().warn(message, e);
        }
        final ModelAndView mav = new ModelAndView();
        mav.addObject("message", message + ". " + e.getClass().getSimpleName());
        if (e != null) {
            mav.addObject("cause", e);
        }
        mav.addObject("applicationVersion", buildProperties.getVersion());
        if (userDetails != null) {
            mav.addObject("Username", userDetails.getUsername());
            final Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            if (authorities != null && authorities.size() > 0) {
                mav.addObject("Role", authorities.iterator().next().getAuthority());
            }
        }
        mav.setViewName(getErrorViewName());
        return mav;
    }

    protected ModelAndView error(final Exception e, final UserDetails userDetails) {
        if (e == null) {
            return error("Failed!", userDetails);
        }

        if (e instanceof ValidationException) {
            return error("Validation failed", e, userDetails);
        }
        if (e instanceof DataIntegrityViolationException) {
            return error("DataIntegrity violation", e, userDetails);
        }
        if (e instanceof EntityExistsException) {
            return error("Object exists", e, userDetails);
        }
        if (e instanceof EntityNotFoundException) {
            return error("Not found", e, userDetails);
        }
        if (e instanceof PersistenceException) {
            return error("Persistence failed", e, userDetails);
        }
        return error(e.getClass().getSimpleName(), e, userDetails);

    }
}
