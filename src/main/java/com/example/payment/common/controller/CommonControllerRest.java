package com.example.payment.common.controller;

import org.apache.commons.logging.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.payment.common.utils.StringUtils;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ValidationException;

/**
 * Common UI controller.
 */
public class CommonControllerRest {

    protected Log getLog() {
        return null;
    }

    protected ResponseStatusException error(final Exception e) {
        return error(null, e);
    }

    protected ResponseStatusException error(final String message, final Exception e) {
        if (getLog() != null) {
            if (!StringUtils.isEmpty(message)) {
                getLog().warn("Error: " + message, e);
            } else if (e != null) {
                getLog().warn("Error: " + e.getMessage(), e);
            } else {
                getLog().error("Exception is null");
            }
        }
        if (e == null) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (e instanceof ValidationException) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        if (e instanceof DataIntegrityViolationException) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        if (e instanceof EntityExistsException) {
            return new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
        if (e instanceof EntityNotFoundException) {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        if (e instanceof EntityNotFoundException) {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        if (e instanceof PersistenceException) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);

    }
}
