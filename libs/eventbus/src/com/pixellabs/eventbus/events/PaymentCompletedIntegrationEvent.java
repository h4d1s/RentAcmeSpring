package com.pixellabs.eventbus.events;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record PaymentCompletedIntegrationEvent(
	UUID correlateId,
	String customerId
) implements BaseIntegrationEvent {}