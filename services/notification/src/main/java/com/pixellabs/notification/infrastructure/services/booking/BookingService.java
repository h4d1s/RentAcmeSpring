package com.pixellabs.notification.infrastructure.services.booking;

import java.util.UUID;

import com.pixellabs.notification.appplication.model.BookingDTO;

public interface BookingService {
	BookingDTO get(UUID id);
}
