package com.pixellabs.notification.infrastructure.exceptions;

import java.io.Serial;


public class BookingIntegrationException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public BookingIntegrationException() {
        super();
    }

    public BookingIntegrationException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BookingIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingIntegrationException(String message) {
        super(message);
    }

    public BookingIntegrationException(Throwable cause) {
        super(cause);
    }
}
