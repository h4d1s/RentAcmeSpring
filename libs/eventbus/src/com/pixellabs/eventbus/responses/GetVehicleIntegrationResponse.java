package com.pixellabs.eventbus.responses;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetVehicleIntegrationResponse extends BaseIntegrationEvent {
	private UUID id;
	private BigDecimal rentalPricePerDay;
	private String registrationPlates;
}
