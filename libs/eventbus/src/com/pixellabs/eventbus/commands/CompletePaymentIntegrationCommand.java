package com.pixellabs.eventbus.commands;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record CompletePaymentIntegrationCommand(
    UUID correlateId,
	UUID userId,
    BigDecimal totalPrice
) implements BaseIntegrationEvent {}