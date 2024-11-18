package com.pixellabs.eventbus.responses;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

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
public class GetVehicleIntegrationResponse extends BaseIntegrationEvent {
	private UUID id;
	private BigDecimal rentalPricePerDay;
	private String registrationPlates;
}
