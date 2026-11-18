package com.pixellabs.libs.eventbus.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

public record BookingReservedIntegrationEvent(
	UUID correlateId,
	UUID bookingId,
	UUID userId,
	UUID vehicleId,
	Instant pickupAt,
	Instant returnAt,
	BigDecimal price
) implements BaseIntegrationEvent {}
