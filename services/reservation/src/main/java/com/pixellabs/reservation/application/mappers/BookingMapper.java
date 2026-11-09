package com.pixellabs.reservation.application.mappers;

import org.mapstruct.Mapper;

import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingCommand;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
	ReserveBookingCommand bookingToReserveBookingRequest(Booking booking);
	Booking reserveBookingRequestToBooking(ReserveBookingCommand request);
}
