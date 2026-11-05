package com.pixellabs.inventory.application.useCases.models.commands.updateModel;

import java.util.UUID;

import com.pixellabs.inventory.domain.modelAggregate.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateModelCommand(
    UUID id,
    @NotBlank String name,
    @Positive int yearOfProduction,
    @Positive int numberOfSeats,
    @NotNull Category category,
    @NotNull UUID brandId
) {}
