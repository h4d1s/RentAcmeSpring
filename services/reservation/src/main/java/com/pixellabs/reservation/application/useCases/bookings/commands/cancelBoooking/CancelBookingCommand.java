package com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking;

import java.util.UUID;

public record CancelBookingCommand (
    UUID id
) {}
