package com.pixellabs.inventory.application.useCases.vehicles.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
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
public class CreateVehicleRequest {
	@DecimalMin(value = "5.00")
    private BigDecimal rentalPricePerDay;
	@NotNull
    private UUID variantId;
	@NotBlank
	private String registrationPlates;
}
