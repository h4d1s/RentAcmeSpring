package com.pixellabs.libs.eventbus.requests;

import java.util.UUID;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

public record GetBookingIntegrationRequest(
	UUID correlateId,
	UUID id
) implements BaseIntegrationEvent {}
