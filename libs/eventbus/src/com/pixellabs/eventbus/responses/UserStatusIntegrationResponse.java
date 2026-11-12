package com.pixellabs.eventbus.responses;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record UserStatusIntegrationResponse(
	UUID correlateId,
	boolean exists
) implements BaseIntegrationEvent {}
