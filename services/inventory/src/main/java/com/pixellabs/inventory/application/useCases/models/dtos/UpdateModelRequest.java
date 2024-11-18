package com.pixellabs.inventory.application.useCases.models.dtos;

import java.util.UUID;

import com.pixellabs.inventory.domain.modelAggregate.Category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateModelRequest {
	@NotBlank
    private String name;
	@Min(value = 1950)
    private int yearOfProduction;
	@Min(value = 1)
    private int numberOfSeats;
	@NotNull
    private Category category;
	@NotNull
    private UUID brandId;
}
