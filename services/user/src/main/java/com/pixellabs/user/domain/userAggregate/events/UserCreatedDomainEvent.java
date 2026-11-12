package com.pixellabs.user.domain.userAggregate.events;

import java.util.UUID;

public record UserCreatedDomainEvent(
	UUID id
) { }
