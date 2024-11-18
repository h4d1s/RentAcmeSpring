package com.pixellabs.reservation.api.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pixellabs.reservation.application.useCases.bookings.CancelBookingUseCase;
import com.pixellabs.reservation.application.useCases.bookings.CompleteBookingUseCase;
import com.pixellabs.reservation.application.useCases.bookings.GetBookingUseCase;
import com.pixellabs.reservation.application.useCases.bookings.GetListBookingUseCase;
import com.pixellabs.reservation.application.useCases.bookings.ReserveBookingUseCase;
import com.pixellabs.reservation.application.useCases.bookings.dtos.CancelBookingRequest;
import com.pixellabs.reservation.application.useCases.bookings.dtos.CompleteBookingRequest;
import com.pixellabs.reservation.application.useCases.bookings.dtos.GetListBookingRequest;
import com.pixellabs.reservation.application.useCases.bookings.dtos.ReserveBookingRequest;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.exceptions.BookingNotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
	private final CancelBookingUseCase cancelBookingUseCase;
	private final CompleteBookingUseCase completeBookingUseCase;
	private final GetBookingUseCase getBookingUseCase;
	private final GetListBookingUseCase getListBookingUseCase;
	private final ReserveBookingUseCase reserveBookingUseCase;
	
	public BookingController(CancelBookingUseCase cancelBookingUseCase, CompleteBookingUseCase completeBookingUseCase,
			GetBookingUseCase getBookingUseCase, GetListBookingUseCase getListBookingUseCase,
			ReserveBookingUseCase reserveBookingUseCase) {
		super();
		this.cancelBookingUseCase = cancelBookingUseCase;
		this.completeBookingUseCase = completeBookingUseCase;
		this.getBookingUseCase = getBookingUseCase;
		this.getListBookingUseCase = getListBookingUseCase;
		this.reserveBookingUseCase = reserveBookingUseCase;
	}
	
	@GetMapping
	public Page<Booking> getAllBookings(@Valid GetListBookingRequest request)
			throws BookingNotFoundException, ReservationDomainException {
		return getListBookingUseCase.execute(request);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Booking> getBooking(@PathVariable("id") UUID id)
			throws BookingNotFoundException {
		Booking booking = getBookingUseCase.execute(id);
		return ResponseEntity.ok(booking);
	}
	
	@PostMapping
	public ResponseEntity<Booking> reserveBooking(@Valid @RequestBody ReserveBookingRequest request)
			throws BookingNotFoundException, ReservationDomainException {
		Booking booking = reserveBookingUseCase.execute(request);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(booking.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(booking);
	}
	
	@PostMapping("/cancel")
	public ResponseEntity<Void> cancelBooking(@Valid @RequestBody CancelBookingRequest request)
			throws BookingNotFoundException, ReservationDomainException {
		cancelBookingUseCase.execute(request);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/complete")
	public ResponseEntity<Booking> completeBooking(@Valid @RequestBody CompleteBookingRequest request)
			throws BookingNotFoundException, ReservationDomainException {
		completeBookingUseCase.execute(request);
		return ResponseEntity.ok().build();
	}
}
