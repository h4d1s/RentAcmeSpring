package com.pixellabs.eventbus.responses;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record VehicleStatusIntegrationResponse (
	UUID correlateId,
	boolean exists
) implements BaseIntegrationEvent {}