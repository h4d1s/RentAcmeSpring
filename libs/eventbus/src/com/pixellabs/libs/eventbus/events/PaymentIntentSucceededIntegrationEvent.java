package com.pixellabs.libs.eventbus.events;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record PaymentIntentSucceededIntegrationEvent(
    UUID correlateId,
    UUID userId
) implements BaseIntegrationEvent {}