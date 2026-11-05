package com.pixellabs.inventory.application.useCases.vehicles.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.inventory.domain.variantAggregate.Gearbox;
import com.pixellabs.inventory.domain.modelAggregate.Category;

public record VehicleSearchResponse(
    UUID id,
    String brand,
    String model,
    BigDecimal rentalPricePerDay,
    Gearbox gearbox,
    Category category,
    int numberOfSeats,
    int power
) {}