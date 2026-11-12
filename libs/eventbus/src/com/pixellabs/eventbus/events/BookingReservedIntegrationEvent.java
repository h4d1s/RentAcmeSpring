package com.pixellabs.eventbus.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record BookingReservedIntegrationEvent(
	UUID correlateId,
	UUID bookingId,
	UUID userId,
	UUID vehicleId,
	LocalDateTime pickupDate,
	LocalDateTime returnDate,
	BigDecimal price
) implements BaseIntegrationEvent {}
