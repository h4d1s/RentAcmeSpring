package com.pixellabs.payment.domain.paymentAggregate.events;

import com.pixellabs.payment.domain.paymentAggregate.Payment;

public record PaymentIntentCanceledDomainEvent(
    Payment payment
) {}
