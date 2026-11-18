package com.pixellabs.libs.eventbus.responses;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record VehicleStatusIntegrationResponse (
	UUID correlateId,
	boolean exists
) implements BaseIntegrationEvent {}