package com.pixellabs.inventory.domain.vehicleAggregate.events;

import java.util.UUID;

public record VehicleDeletedDomainEvent(
    UUID id
) {}
