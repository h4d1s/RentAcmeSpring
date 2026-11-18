package com.pixellabs.libs.eventbus.events;

import java.util.UUID;

import com.pixellabs.libs.eventbus.BaseIntegrationEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreatedIntegrationEvent(
    @NotNull
    UUID correlateId,
    @NotNull
    UUID id,
    @NotBlank
    String email,
    String firstName,
    String lastName
) implements BaseIntegrationEvent {}
