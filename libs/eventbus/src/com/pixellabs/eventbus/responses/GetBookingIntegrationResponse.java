package com.pixellabs.eventbus.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetBookingIntegrationResponse (
    UUID id,
    UUID vehicleId,
    UUID userId,
    LocalDate pickupDate,
    LocalDate returnDate,
    LocalDateTime bookingDate,
    BigDecimal price
) {}
