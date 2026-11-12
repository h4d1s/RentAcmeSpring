package com.pixellabs.eventbus;

import java.util.UUID;

public interface BaseIntegrationEvent {
	UUID correlateId();
}
