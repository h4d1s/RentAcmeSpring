package com.pixellabs.eventbus.events;

public record KeycloakAuthDetails(
    String authMethod,
    String authType,
    String redirectUri,
    String codeId,
    String username
) {}
