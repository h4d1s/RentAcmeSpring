package com.pixellabs.eventbus.events;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingCompletedIntegrationEvent(
	UUID correlateId,
	UUID bookingId,
	UUID vehicleId,
	LocalDateTime pickupDate,
	LocalDateTime returnDate
) implements BaseIntegrationEvent {}
