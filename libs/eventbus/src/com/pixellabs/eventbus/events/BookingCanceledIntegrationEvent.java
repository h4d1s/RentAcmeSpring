package com.pixellabs.eventbus.events;

import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record BookingCanceledIntegrationEvent(
    UUID correlateId,
	UUID bookingId,
    UUID vehicleId
) implements BaseIntegrationEvent {}
