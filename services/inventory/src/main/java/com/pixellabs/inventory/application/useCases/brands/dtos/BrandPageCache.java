package com.pixellabs.inventory.application.useCases.brands.dtos;

import com.pixellabs.inventory.domain.brandAggregate.Brand;

import java.util.List;

public record BrandPageCache(
    List<Brand> content,
    int page,
    int size,
    long totalElements
) {}
