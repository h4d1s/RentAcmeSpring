package com.pixellabs.libs.user.grpc.models;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String externalId,
    String firstName,
    String lastName,
    String email
) {}