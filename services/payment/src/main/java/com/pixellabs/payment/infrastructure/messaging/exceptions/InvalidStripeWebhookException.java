package com.pixellabs.payment.infrastructure.messaging.exceptions;

public class InvalidStripeWebhookException extends RuntimeException {
    public InvalidStripeWebhookException(String message) {
        super(message);
    }
}
