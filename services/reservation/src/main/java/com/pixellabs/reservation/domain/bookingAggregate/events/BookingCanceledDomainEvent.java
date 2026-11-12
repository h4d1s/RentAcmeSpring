package com.pixellabs.reservation.domain.bookingAggregate.events;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;

public record BookingCanceledDomainEvent(
	Booking booking
) {}
