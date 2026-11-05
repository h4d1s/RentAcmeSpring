package com.pixellabs.inventory.application.useCases.brands.commands.createBrand;

import jakarta.validation.constraints.NotBlank;

public record CreateBrandCommand(
    @NotBlank
    String name
) {}
