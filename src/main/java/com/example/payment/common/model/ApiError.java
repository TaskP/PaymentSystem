package com.example.payment.common.model;

import org.springframework.http.HttpStatus;

public class ApiError {

    private final int apiError = 1;

    private final HttpStatus status;

    private final String message;

    private final String error;

    public ApiError(final HttpStatus status, final String message, final Exception exception) {
        super();
        this.status = status;
        this.message = message;
        if (exception != null) {
            this.error = exception.getMessage();
        } else {
            this.error = "";
        }
    }

    public int getApiError() {
        return apiError;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
