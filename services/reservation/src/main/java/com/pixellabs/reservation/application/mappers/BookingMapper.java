package com.pixellabs.reservation.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pixellabs.reservation.application.useCases.bookings.dtos.ReserveBookingRequest;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
	BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);
	
	ReserveBookingRequest bookingToReserveBookingRequest(Booking booking);
	Booking reserveBookingRequestToBooking(ReserveBookingRequest request);
}
