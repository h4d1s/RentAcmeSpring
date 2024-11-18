package com.pixellabs.reservation.application.useCases.bookings;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.bookingAggregate.exceptions.BookingNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetBookingUseCase {
	@Autowired
	private final BookingRepository bookingRepository;
	
	public GetBookingUseCase(BookingRepository bookingRepository) {
		super();
		this.bookingRepository = bookingRepository;
	}
	
	@Transactional
	public Booking execute(UUID id) throws BookingNotFoundException {
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new BookingNotFoundException());
		return booking;
	}
}
