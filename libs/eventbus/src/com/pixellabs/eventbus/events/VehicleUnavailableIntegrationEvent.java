package com.pixellabs.eventbus.events;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import java.util.UUID;

public record VehicleUnavailableIntegrationEvent(
    UUID correlateId,
    UUID bookingId
) implements BaseIntegrationEvent {}
