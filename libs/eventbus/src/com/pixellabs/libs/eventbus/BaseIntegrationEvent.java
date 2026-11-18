package com.pixellabs.libs.eventbus;

import java.util.UUID;

public interface BaseIntegrationEvent {
	UUID correlateId();
}
