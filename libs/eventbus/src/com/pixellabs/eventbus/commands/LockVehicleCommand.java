package com.pixellabs.eventbus.commands;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record LockVehicleCommand(
    UUID correlateId,
    UUID bookingId,
    UUID vehicleId
) implements BaseIntegrationEvent {}
