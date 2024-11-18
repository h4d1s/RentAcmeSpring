package com.pixellabs.reservation.application.useCases.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.requests.GetVehicleIntegrationRequest;
import com.pixellabs.eventbus.responses.GetVehicleIntegrationResponse;
import com.pixellabs.reservation.application.useCases.bookings.dtos.ReserveBookingRequest;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.bookingAggregate.exceptions.BookingNotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;
import com.pixellabs.reservation.infrastructure.rabbitmq.IntegrationEventService;

import jakarta.transaction.Transactional;

@Component
public class ReserveBookingUseCase {
	@Autowired
	private final BookingRepository bookingRepository;
	
	@Autowired
	private final IntegrationEventService integrationService;
	
	public ReserveBookingUseCase(
			BookingRepository bookingRepository,
			IntegrationEventService integrationService) {
		super();
		this.bookingRepository = bookingRepository;
		this.integrationService = integrationService;
	}
	
	@Transactional
	public Booking execute(ReserveBookingRequest request) throws BookingNotFoundException, ReservationDomainException {
		Booking newBooking = new Booking(request.getVehicleId(), request.getUserId(), request.getPickupDate(), request.getReturnDate());
		
		GetVehicleIntegrationRequest getVehicleRequest = new GetVehicleIntegrationRequest(newBooking.getVehicleId());
		GetVehicleIntegrationResponse response = (GetVehicleIntegrationResponse) integrationService.sendAndReceive(getVehicleRequest, BusConstants.EXCHANGE_INVENTORY, BusConstants.ROUTING_KEY_VEHICLE_CHECK_REQUEST);

		newBooking.setPrice(response.getRentalPricePerDay());
		
		Booking booking = bookingRepository.save(newBooking);
		return booking;
	}
}
