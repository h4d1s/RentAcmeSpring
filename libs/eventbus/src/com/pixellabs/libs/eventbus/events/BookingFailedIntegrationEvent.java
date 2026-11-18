package com.pixellabs.libs.eventbus.events;

import java.util.UUID;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

public record BookingFailedIntegrationEvent(
	UUID correlateId,
	UUID bookingId
) implements BaseIntegrationEvent {}
