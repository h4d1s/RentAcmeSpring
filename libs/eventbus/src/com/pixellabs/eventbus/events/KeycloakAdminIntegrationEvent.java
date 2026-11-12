package com.pixellabs.eventbus.events;

public record KeycloakAdminIntegrationEvent(
    Long time,
    String realmId,
    KeycloakAuthDetails keycloakAuthDetails,
    String resourceType,
    String resourceTypeAsString,
    String operationType,
    String resourcePath,
    String representation,
    String error
) {}

