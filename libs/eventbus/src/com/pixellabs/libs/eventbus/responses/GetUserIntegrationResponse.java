package com.pixellabs.libs.eventbus.responses;

import java.util.UUID;

public record GetUserIntegrationResponse(
    UUID id,
    String firstName,
	String lastName,
    String email
) {}
