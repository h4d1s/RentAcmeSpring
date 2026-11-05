package com.pixellabs.inventory.application.useCases.brands.commands.updateBrand;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateBrandCommand(
    UUID id,
    @NotBlank String name
) {}
