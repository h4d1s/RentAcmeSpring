package com.pixellabs.inventory.application.useCases.variants.dtos;

import java.util.UUID;

import com.pixellabs.inventory.domain.variantAggregate.FuelType;
import com.pixellabs.inventory.domain.variantAggregate.Gearbox;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class UpdateVariantRequest {
	@NotNull
	private UUID id;
	@NotBlank
    private String name;
	@NotNull
    private Gearbox gearbox;
	@NotNull
    private FuelType fuelType;
	@Positive
    private int power;
	@Positive
    private int engineSize;
	@NotNull
    private UUID modelId;
}