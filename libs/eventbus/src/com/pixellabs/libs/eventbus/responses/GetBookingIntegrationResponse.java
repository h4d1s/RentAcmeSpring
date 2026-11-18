package com.pixellabs.libs.eventbus.responses;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record GetBookingIntegrationResponse (
    UUID id,
    UUID vehicleId,
    UUID userId,
    Instant pickupAt,
    Instant returnAt,
    Instant bookingAt,
    BigDecimal price
) {}
