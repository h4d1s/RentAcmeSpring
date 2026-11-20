package com.pixellabs.notification.appplication.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BookingDTO(
	UUID id,
    UUID vehicleId,
    UUID userId,
    Instant pickupDate,
    Instant returnDate,
    Instant bookingDate,
    BigDecimal price
) {}