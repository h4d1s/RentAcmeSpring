package com.pixellabs.payment.domain.exceptions;

import java.io.Serial;

public class PaymentDomainException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public PaymentDomainException() {
        super();
    }

    public PaymentDomainException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(Throwable cause) {
        super(cause);
    }
}