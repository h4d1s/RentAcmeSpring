package com.pixellabs.eventbus.events;

import java.util.List;
import java.util.Map;

public record KeycloakUserRepresentation(
    String username,
    String firstName,
    String lastName,
    String email,
    Boolean emailVerified,
    Boolean enabled,
    Map<String, List<String>> attributes,
    List<String> requiredActions,
    List<String> groups
) {}
