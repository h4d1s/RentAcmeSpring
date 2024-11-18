package com.pixellabs.reservation.application.domainEventHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCanceledIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCanceledDomainEvent;
import com.pixellabs.reservation.infrastructure.rabbitmq.IntegrationEventService;

@Component
public class BookingCanceledDomainEventHandler {
	@Autowired
	private IntegrationEventService service;
	
	public BookingCanceledDomainEventHandler(IntegrationEventService service) {
		super();
		this.service = service;
	}

	@EventListener
    public void handle(BookingCanceledDomainEvent event) {
        Booking booking = event.getBooking();
        BookingCanceledIntegrationEvent bookingCanceledEvent = new BookingCanceledIntegrationEvent(booking.getId(), booking.getVehicleId());
        service.publishEvent(bookingCanceledEvent, BusConstants.EXCHANGE_BOOKING, BusConstants.ROUTING_KEY_BOOKING_CANCELED);
    }
}
