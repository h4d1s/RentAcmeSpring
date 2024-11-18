package com.pixellabs.reservation.domain.bookingAggregate.events;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingCompletedDomainEvent {
	private Booking booking;
}