package com.pixellabs.reservation.infrastructure.grpc.user.dto;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String externalId,
    String firstName,
    String lastName,
    String email
) {}