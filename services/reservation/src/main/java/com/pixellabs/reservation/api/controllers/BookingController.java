package com.pixellabs.reservation.api.controllers;

import java.net.URI;
import java.util.UUID;

import com.pixellabs.reservation.application.useCases.bookings.queries.getBooking.GetBookingQuery;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList.GetBookingListQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking.CancelBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.commands.completeBooking.CompleteBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBooking.GetBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList.GetBookingListHandler;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking.CancelBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.completeBooking.CompleteBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingCommand;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
	private final CancelBookingHandler cancelBookingHandler;
	private final CompleteBookingHandler completeBookingHandler;
	private final GetBookingHandler getBookingHandler;
	private final GetBookingListHandler getBookingListHandler;
	private final ReserveBookingHandler reserveBookingHandler;
	
	@GetMapping
	@PreAuthorize("@bookingAuthorization.canViewAll(authentication)")
	public Page<Booking> getAllBookings(@Valid GetBookingListQuery query)
			throws NotFoundException {
		return getBookingListHandler.handle(query);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("@bookingAuthorization.canView(authentication, #id)")
	public ResponseEntity<Booking> getBooking(@PathVariable("id") UUID id)
			throws NotFoundException {
		Booking booking = getBookingHandler.handle(new GetBookingQuery(id));
		return ResponseEntity.ok(booking);
	}
	
	@PostMapping
	@PreAuthorize("@bookingAuthorization.canReserve(authentication)")
	public ResponseEntity<Booking> reserveBooking(@Valid @RequestBody ReserveBookingCommand command)
        throws NotFoundException, ReservationDomainException {
		Booking booking = reserveBookingHandler.handle(command);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(booking.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(booking);
	}
	
	@PostMapping("/cancel")
	@PreAuthorize("@bookingAuthorization.canCancel(authentication)")
	public ResponseEntity<Void> cancelBooking(@Valid @RequestBody CancelBookingCommand command)
			throws NotFoundException, ReservationDomainException {
		cancelBookingHandler.handle(command);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/complete")
	@PreAuthorize("@bookingAuthorization.canComplete(authentication)")
	public ResponseEntity<Booking> completeBooking(@Valid @RequestBody CompleteBookingCommand command)
			throws NotFoundException, ReservationDomainException {
		completeBookingHandler.handle(command);
		return ResponseEntity.ok().build();
	}
}
