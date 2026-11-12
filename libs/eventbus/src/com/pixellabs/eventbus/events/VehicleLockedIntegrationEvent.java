package com.pixellabs.eventbus.events;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record VehicleLockedIntegrationEvent(
    UUID correlateId,
    UUID bookingId,
    UUID vehicleId
) implements BaseIntegrationEvent {}
