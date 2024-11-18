package com.pixellabs.reservation.domain.bookingAggregate.events;

import org.springframework.context.ApplicationEvent;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingReservedDomainEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Booking booking;
	
	public BookingReservedDomainEvent(Object source, Booking booking) {
		super(source);
		this.booking = booking;
	}
}