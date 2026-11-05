package com.pixellabs.inventory.application.useCases.models.commands.createModel;

import com.pixellabs.inventory.domain.modelAggregate.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateModelCommand(
    @NotBlank String name,
    @Positive int yearOfProduction,
    @Positive int numberOfSeats,
    @NotNull Category category,
    @NotNull UUID brandId
) {}
