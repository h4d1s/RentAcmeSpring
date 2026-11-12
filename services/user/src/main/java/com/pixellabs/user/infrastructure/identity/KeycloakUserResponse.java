package com.pixellabs.user.infrastructure.identity;

public record KeycloakUserResponse(
    String id,
    String username,
    String firstName,
    String lastName,
    String email,
    Boolean enabled
) {}
