package com.pixellabs.libs.eventbus.commands;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

public record CompletePaymentIntegrationCommand(
    UUID correlateId,
	UUID userId,
    BigDecimal totalPrice
) implements BaseIntegrationEvent {}