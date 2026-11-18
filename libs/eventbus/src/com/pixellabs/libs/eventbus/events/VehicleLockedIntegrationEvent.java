package com.pixellabs.libs.eventbus.events;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record VehicleLockedIntegrationEvent(
    UUID correlateId,
    UUID bookingId,
    UUID vehicleId
) implements BaseIntegrationEvent {}
