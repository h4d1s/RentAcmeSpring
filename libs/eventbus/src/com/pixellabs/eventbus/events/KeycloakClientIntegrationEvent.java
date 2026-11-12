package com.pixellabs.eventbus.events;

public record KeycloakClientIntegrationEvent(
    String className,
    Long time,
    String resourceType,
    String operationType,
    String type,
    String realmId,
    String clientId,
    String userId,
    String sessionId,
    String ipAddress,
    String error,
    String representation,
    KeycloakAuthDetails keycloakAuthDetails
) {}