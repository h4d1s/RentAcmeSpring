package com.pixellabs.libs.eventbus.commands;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record LockVehicleCommand(
    UUID correlateId,
    UUID bookingId,
    UUID vehicleId
) implements BaseIntegrationEvent {}
