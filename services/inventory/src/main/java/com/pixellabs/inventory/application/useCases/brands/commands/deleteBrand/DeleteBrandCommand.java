package com.pixellabs.inventory.application.useCases.brands.commands.deleteBrand;

import java.util.UUID;

public record DeleteBrandCommand(
    UUID id
) {}
