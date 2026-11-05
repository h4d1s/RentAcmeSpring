package com.pixellabs.inventory.application.useCases.vehicles.dtos;

import java.util.List;

public record SearchVehiclePageCache(
    List<VehicleSearchResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {}