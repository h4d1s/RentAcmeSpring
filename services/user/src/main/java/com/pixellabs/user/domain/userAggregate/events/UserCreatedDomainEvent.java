package com.pixellabs.user.domain.userAggregate.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreatedDomainEvent {
	private UUID id;
}
