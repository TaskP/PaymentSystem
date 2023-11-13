package com.example.payment.common.controller;

import org.apache.commons.logging.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.payment.common.model.ApiError;
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

    protected ApiError apiError(final String message, final Exception e) {
        if (getLog() != null) {
            if (!StringUtils.isEmpty(message)) {
                getLog().warn("ApiError: " + message, e);
            } else if (e != null) {
                getLog().warn("ApiError: " + e.getMessage(), e);
            } else {
                getLog().error("ApiError Exception is null");
            }
        }
        if (e == null) {
            return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
        }
        if (e instanceof ValidationException) {
            return new ApiError(HttpStatus.BAD_REQUEST, message + " Validation Error", e);
        }
        if (e instanceof DataIntegrityViolationException) {
            return new ApiError(HttpStatus.BAD_REQUEST, message + " DataIntegrity Violation", e);
        }
        if (e instanceof EntityExistsException) {
            return new ApiError(HttpStatus.CONFLICT, message + " Entity Exists", e);
        }
        if (e instanceof EntityNotFoundException) {
            return new ApiError(HttpStatus.NOT_FOUND, message + " Entity Not Found", e);
        }
        if (e instanceof PersistenceException) {
            return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message + " Persistence Error", e);
        }
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message + " Internal server error", e);
    }

    protected ResponseStatusException error(final String message, final Exception e) {
        if (getLog() != null) {
            if (!StringUtils.isEmpty(message)) {
                getLog().warn("Error: " + message, e);
            } else if (e != null) {
                getLog().warn("Error: " + e.getMessage(), e);
            } else {
                getLog().error("Error: Exception is null");
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
        if (e instanceof PersistenceException) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
}
