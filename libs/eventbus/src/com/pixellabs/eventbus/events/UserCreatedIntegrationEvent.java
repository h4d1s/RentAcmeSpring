package com.pixellabs.eventbus.events;

import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

public record UserCreatedIntegrationEvent(
    UUID correlateId,
    UUID id,
    String email,
    String firstName,
    String lastName
) implements BaseIntegrationEvent {}
