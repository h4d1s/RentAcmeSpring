package com.pixellabs.libs.eventbus.commands;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentIntentCommand(
    @NotNull
    UUID correlateId,

    @NotNull
    UUID userId,

    @NotNull
    @Positive
    BigDecimal totalPrice
) implements BaseIntegrationEvent {}
