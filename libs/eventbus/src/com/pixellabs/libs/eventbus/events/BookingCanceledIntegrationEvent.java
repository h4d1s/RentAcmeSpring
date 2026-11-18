package com.pixellabs.libs.eventbus.events;

import java.util.UUID;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

public record BookingCanceledIntegrationEvent(
    UUID correlateId,
	UUID bookingId,
    UUID vehicleId
) implements BaseIntegrationEvent {}
