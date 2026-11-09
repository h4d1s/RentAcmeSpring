package com.pixellabs.reservation.application.useCases.bookings.commands.completeBooking;

import java.util.UUID;

public record CompleteBookingCommand(
	UUID id
) {}
