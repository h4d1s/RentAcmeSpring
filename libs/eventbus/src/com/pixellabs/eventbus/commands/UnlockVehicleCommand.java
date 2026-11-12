package com.pixellabs.eventbus.commands;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record UnlockVehicleCommand(
    UUID correlateId,
    UUID bookingId,
    UUID vehicleId
) implements BaseIntegrationEvent {}