package com.pixellabs.inventory.application.useCases.vehicles.commands.updateVehicle;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateVehicleCommand(
    UUID id,
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal rentalPricePerDay,
    @NotNull UUID variantId,
    @NotBlank String registrationPlates
) {}
