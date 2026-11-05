package com.pixellabs.inventory.application.useCases.variants.dtos;

import com.pixellabs.inventory.domain.variantAggregate.Variant;

import java.util.List;

public record VariantPageCache(
    List<Variant> content,
    int page,
    int size,
    long totalElements
) {}
