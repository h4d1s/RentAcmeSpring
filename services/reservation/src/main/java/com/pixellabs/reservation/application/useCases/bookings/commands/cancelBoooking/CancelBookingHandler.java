package com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking;

import lombok.RequiredArgsConstructor;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelBookingHandler {
	private final BookingRepository bookingRepository;

	@CacheEvict(value = "bookings", allEntries = true)
	@Transactional
	public void handle(CancelBookingCommand command)
		    throws NotFoundException, ReservationDomainException {
		Booking booking = bookingRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("Booking not found."));
		
		booking.updateCanceledStatus();
		bookingRepository.save(booking);
	}
}