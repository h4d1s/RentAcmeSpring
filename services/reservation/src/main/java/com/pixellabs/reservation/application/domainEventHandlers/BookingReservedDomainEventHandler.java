package com.pixellabs.reservation.application.domainEventHandlers;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingReservedIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingReservedDomainEvent;

@Component
@RequiredArgsConstructor
public class BookingReservedDomainEventHandler {
	private final RabbitTemplate rabbitTemplate;

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

		rabbitTemplate.convertAndSend(
			BusConstants.EXCHANGE_BOOKING,
			BusConstants.ROUTING_KEY_BOOKING_RESERVED,
			bookingReservedEvent
		);
    }
}
