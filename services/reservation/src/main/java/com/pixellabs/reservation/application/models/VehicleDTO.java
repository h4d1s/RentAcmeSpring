package com.pixellabs.reservation.application.models;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
	private UUID id;
	private BigDecimal rentalPricePerDay;
	private String registrationPlates;
}
