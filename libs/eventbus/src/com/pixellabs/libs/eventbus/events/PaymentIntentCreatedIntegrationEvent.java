package com.pixellabs.libs.eventbus.events;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record PaymentIntentCreatedIntegrationEvent(
    UUID correlateId,
    UUID userId,
    String paymentIntentId
) implements BaseIntegrationEvent {}
