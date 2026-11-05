package com.pixellabs.inventory.application.useCases.variants.commands.deleteVariant;

import java.util.UUID;

public record DeleteVariantCommand(
    UUID id
) {}