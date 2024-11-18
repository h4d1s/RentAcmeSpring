package com.pixellabs.reservation.application.domainEventHandlers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingReservedIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingReservedDomainEvent;
import com.pixellabs.reservation.infrastructure.rabbitmq.IntegrationEventService;

@Component
public class BookingReservedDomainEventHandler {
	@Autowired
	private IntegrationEventService service;

	public BookingReservedDomainEventHandler(IntegrationEventService service) {
		super();
		this.service = service;
	}

	@EventListener
    public void handle(BookingReservedDomainEvent event) {
		Booking booking = event.getBooking();
        BookingReservedIntegrationEvent bookingReservedEvent = new BookingReservedIntegrationEvent(
        		booking.getId(),
        		booking.getUserId(),
        		booking.getVehicleId(),
        		booking.getPickupDate(),
        		booking.getReturnDate(),
        		booking.getPrice());
        bookingReservedEvent.setCorrelateId(UUID.randomUUID());
        
        System.out.println(">>> " + bookingReservedEvent.getCorrelateId());
        
        service.publishEvent(
        		bookingReservedEvent,
        		BusConstants.EXCHANGE_BOOKING,
        		BusConstants.ROUTING_KEY_BOOKING_RESERVED);
    }
}
