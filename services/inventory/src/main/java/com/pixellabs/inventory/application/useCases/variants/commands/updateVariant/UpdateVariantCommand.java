package com.pixellabs.inventory.application.useCases.variants.commands.updateVariant;

import java.util.UUID;

import com.pixellabs.inventory.domain.variantAggregate.FuelType;
import com.pixellabs.inventory.domain.variantAggregate.Gearbox;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateVariantCommand(
    UUID id,
    @NotBlank String name,
    @NotNull Gearbox gearbox,
    @NotNull FuelType fuelType,
    @Positive int power,
    @Positive int engineSize,
    @NotNull UUID modelId
) {}