package com.pixellabs.libs.eventbus.events;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record VehicleUnavailableIntegrationEvent(
    UUID correlateId,
    UUID bookingId
) implements BaseIntegrationEvent {}
