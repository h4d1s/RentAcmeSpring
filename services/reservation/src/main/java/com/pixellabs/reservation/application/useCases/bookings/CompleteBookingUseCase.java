package com.pixellabs.reservation.application.useCases.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.reservation.application.useCases.bookings.dtos.CompleteBookingRequest;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.bookingAggregate.exceptions.BookingNotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.transaction.Transactional;

@Component
public class CompleteBookingUseCase {
	@Autowired
	private final BookingRepository bookingRepository;
	
	public CompleteBookingUseCase(BookingRepository bookingRepository) {
		super();
		this.bookingRepository = bookingRepository;
	}
	
	@Transactional
	public void execute(CompleteBookingRequest request) throws BookingNotFoundException, ReservationDomainException {		
		Booking booking = bookingRepository.findById(request.getId())
				.orElseThrow(() -> new BookingNotFoundException());
		
		booking.setCompleteStatus();
		bookingRepository.save(booking);
	}
}
