package com.pixellabs.eventbus.events;

import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record PaymentFailedIntegrationEvent(
	UUID correlateId,
	UUID userId
) implements BaseIntegrationEvent {}