package com.pixellabs.inventory.application.useCases.vehicles.commands.deleteVehicle;

import java.util.UUID;

public record DeleteVehicleCommand(
    UUID id
) {}