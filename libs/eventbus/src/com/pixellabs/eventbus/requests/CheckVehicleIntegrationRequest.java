package com.pixellabs.eventbus.requests;

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
public class CheckVehicleIntegrationRequest extends BaseIntegrationEvent {
	private UUID id;
}