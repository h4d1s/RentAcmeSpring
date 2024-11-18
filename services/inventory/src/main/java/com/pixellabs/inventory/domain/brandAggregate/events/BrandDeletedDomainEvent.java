package com.pixellabs.inventory.domain.brandAggregate.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BrandDeletedDomainEvent {
	private UUID id;
}
