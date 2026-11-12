package com.pixellabs.eventbus.events;

import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record BookingFailedIntegrationEvent(
	UUID correlateId,
	UUID bookingId
) implements BaseIntegrationEvent {}
