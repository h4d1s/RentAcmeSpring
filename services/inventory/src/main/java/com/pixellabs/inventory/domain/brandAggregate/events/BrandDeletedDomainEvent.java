package com.pixellabs.inventory.domain.brandAggregate.events;

import java.util.UUID;

public record BrandDeletedDomainEvent(
	UUID id
) {}
