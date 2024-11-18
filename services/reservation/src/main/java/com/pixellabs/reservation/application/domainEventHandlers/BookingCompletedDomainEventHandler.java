package com.pixellabs.reservation.application.domainEventHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCompletedIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCompletedDomainEvent;
import com.pixellabs.reservation.infrastructure.rabbitmq.IntegrationEventService;

@Component
public class BookingCompletedDomainEventHandler {
	@Autowired
	private IntegrationEventService service;
	
	public BookingCompletedDomainEventHandler(IntegrationEventService service) {
		super();
		this.service = service;
	}

	@EventListener
    public void handle(BookingCompletedDomainEvent event) {
        Booking booking = event.getBooking();
        BookingCompletedIntegrationEvent integrationEvent = new BookingCompletedIntegrationEvent(
        		booking.getId(),
        		booking.getVehicleId(),
        		booking.getPickupDate(),
        		booking.getReturnDate());
        service.publishEvent(integrationEvent, BusConstants.EXCHANGE_BOOKING, BusConstants.ROUTING_KEY_BOOKING_COMPLETED);
    }
}
