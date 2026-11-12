package com.pixellabs.eventbus.requests;

import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record GetBookingIntegrationRequest(
	UUID correlateId,
	UUID id
) implements BaseIntegrationEvent {}
