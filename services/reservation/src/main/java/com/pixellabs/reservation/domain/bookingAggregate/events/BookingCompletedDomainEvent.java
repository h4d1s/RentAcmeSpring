package com.pixellabs.reservation.domain.bookingAggregate.events;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;

public record BookingCompletedDomainEvent(
	Booking booking
) {}