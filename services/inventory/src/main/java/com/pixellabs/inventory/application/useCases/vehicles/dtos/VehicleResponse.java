package com.pixellabs.inventory.application.useCases.vehicles.dtos;

import java.util.UUID;

public record VehicleResponse(
    UUID id,
    String brand,
    String model,
    String variant
) {}