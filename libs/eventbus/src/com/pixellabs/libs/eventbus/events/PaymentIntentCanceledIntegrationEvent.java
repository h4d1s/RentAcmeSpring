package com.pixellabs.libs.eventbus.events;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentIntentCanceledIntegrationEvent(
    @NotNull
    UUID correlateId,
    @NotNull
    UUID paymentId,
    @NotNull
    UUID bookingId
) implements BaseIntegrationEvent {}
