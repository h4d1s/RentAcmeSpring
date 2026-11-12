package com.pixellabs.eventbus.responses;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record GetVehicleIntegrationResponse(
	UUID correlateId,
	UUID id,
	BigDecimal rentalPricePerDay,
	String registrationPlates
) implements BaseIntegrationEvent {}
