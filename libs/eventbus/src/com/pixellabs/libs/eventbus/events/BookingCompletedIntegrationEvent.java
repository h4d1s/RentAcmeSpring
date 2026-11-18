package com.pixellabs.libs.eventbus.events;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.time.Instant;
import java.util.UUID;

public record BookingCompletedIntegrationEvent(
	UUID correlateId,
	UUID bookingId,
	UUID vehicleId,
	Instant pickupAt,
	Instant returnAt
) implements BaseIntegrationEvent {}
