package com.pixellabs.reservation.application.useCases.bookings.queries.getBooking;

import lombok.RequiredArgsConstructor;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.application.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBookingHandler {
	private final BookingRepository bookingRepository;

	@Cacheable(value = "booking", key = "#query.id()")
	@Transactional
	public Booking handle(GetBookingQuery query)
			throws NotFoundException {
		return bookingRepository.findById(query.id())
				.orElseThrow(() -> new NotFoundException("Booking not found"));
	}
}
