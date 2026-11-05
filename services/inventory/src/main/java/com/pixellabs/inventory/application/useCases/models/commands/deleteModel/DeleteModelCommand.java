package com.pixellabs.inventory.application.useCases.models.commands.deleteModel;

import java.util.UUID;

public record DeleteModelCommand(
    UUID id
) {}
