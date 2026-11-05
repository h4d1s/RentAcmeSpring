package com.pixellabs.inventory.application.useCases.vehicles.dtos;

import java.util.List;

public record VehiclePageCache(
    List<VehicleResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {}
