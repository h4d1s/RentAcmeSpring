package com.pixellabs.inventory.application.useCases.models.dtos;

import com.pixellabs.inventory.domain.modelAggregate.Model;

import java.util.List;

public record ModelPageCache(
    List<Model> content,
    int page,
    int size,
    long totalElements
) {}
