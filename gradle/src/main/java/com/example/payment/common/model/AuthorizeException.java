package com.example.payment.common.model;

/**
 * Exception when authorization fails.
 */
@SuppressWarnings("serial")
public class AuthorizeException extends RuntimeException {
    /**
     * Constructor for AuthorizeException.
     *
     * @param msg the detail message
     */
    public AuthorizeException(final String msg) {
        super(msg);
    }

    /**
     * Constructor for AuthorizeException.
     *
     * @param msg   the detail message
     * @param cause the root cause from the data access API in use
     */
    public AuthorizeException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
