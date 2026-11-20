package com.pixellabs.notification.infrastructure.exceptions;

import java.io.Serial;

public class UserIntegrationException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public UserIntegrationException() {
        super();
    }

    public UserIntegrationException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIntegrationException(String message) {
        super(message);
    }

    public UserIntegrationException(Throwable cause) {
        super(cause);
    }
}

