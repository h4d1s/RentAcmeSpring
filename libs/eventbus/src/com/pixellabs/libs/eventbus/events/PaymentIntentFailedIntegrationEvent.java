package com.pixellabs.libs.eventbus.events;

import java.util.UUID;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

public record PaymentIntentFailedIntegrationEvent(
	UUID correlateId,
	UUID userId
) implements BaseIntegrationEvent {}