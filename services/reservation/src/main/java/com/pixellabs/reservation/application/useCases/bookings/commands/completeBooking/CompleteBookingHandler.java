package com.pixellabs.reservation.application.useCases.bookings.commands.completeBooking;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompleteBookingHandler {
	private final BookingRepository bookingRepository;

	@CacheEvict(value = "bookings", allEntries = true)
	@Transactional
	public void handle(CompleteBookingCommand command)
		throws NotFoundException, ReservationDomainException {
		Booking booking = bookingRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("Booking not found."));
		
		booking.updateCompleteStatus();
		bookingRepository.save(booking);
	}
}
