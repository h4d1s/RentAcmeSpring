package com.pixellabs.inventory.domain.vehicleAggregate.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VehicleDeletedDomainEvent {
    private UUID id;
}
