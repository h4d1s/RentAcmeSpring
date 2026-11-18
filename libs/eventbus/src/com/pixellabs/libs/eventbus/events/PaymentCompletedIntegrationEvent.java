package com.pixellabs.libs.eventbus.events;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record PaymentCompletedIntegrationEvent(
	UUID correlateId,
	UUID bookingId
) implements BaseIntegrationEvent {}