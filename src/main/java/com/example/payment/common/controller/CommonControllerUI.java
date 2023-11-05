package com.example.payment.common.controller;

import org.apache.commons.logging.Log;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ValidationException;

/**
 * Common UI controller.
 */
public class CommonControllerUI {

    protected Log getLog() {
        return null;
    }

    protected String getErrorViewName() {
        return "error";
    }

    protected ModelAndView error(final int errno, final String message) {
        if (getLog() != null) {
            getLog().warn("Errno:" + errno + " Error: " + message);
        }

        final ModelAndView mav = new ModelAndView();
        mav.addObject("errno", errno);
        mav.addObject("message", message);
        mav.setViewName(getErrorViewName());
        return mav;
    }

    protected ModelAndView error(final String message) {
        if (getLog() != null) {
            getLog().warn("Error: " + message);
        }
        final ModelAndView mav = new ModelAndView();
        mav.addObject("message", message);
        mav.setViewName(getErrorViewName());
        return mav;
    }

    protected ModelAndView error(final String message, final Exception e) {
        if (getLog() != null) {
            getLog().warn(message, e);
        }
        final ModelAndView mav = new ModelAndView();
        mav.addObject("message", message + ". " + e.getClass().getSimpleName());
        if (e != null) {
            mav.addObject("cause", e);
        }
        mav.setViewName(getErrorViewName());
        return mav;
    }

    protected ModelAndView error(final Exception e) {
        if (e == null) {
            return error("Failed!");
        }

        if (e instanceof ValidationException) {
            return error("Validation failed", e);
        }
        if (e instanceof EntityExistsException) {
            return error("Object exists", e);
        }
        if (e instanceof EntityNotFoundException) {
            return error("Not found", e);
        }
        if (e instanceof PersistenceException) {
            return error("Persistence failed", e);
        }
        return error(e.getClass().getSimpleName(), e);

    }
}
