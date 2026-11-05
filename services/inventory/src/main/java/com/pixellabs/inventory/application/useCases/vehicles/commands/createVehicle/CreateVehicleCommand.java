package com.pixellabs.inventory.application.useCases.vehicles.commands.createVehicle;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateVehicleCommand(
    @NotNull @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal rentalPricePerDay,

    @NotBlank
    String registrationPlates,

    @PositiveOrZero
    int mileageKm,

    @NotNull
    UUID variantId
) {}
