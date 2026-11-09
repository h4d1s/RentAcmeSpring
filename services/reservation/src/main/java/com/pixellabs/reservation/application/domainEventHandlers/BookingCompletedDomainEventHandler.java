package com.pixellabs.reservation.application.domainEventHandlers;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCompletedIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCompletedDomainEvent;

@Component
@RequiredArgsConstructor
public class BookingCompletedDomainEventHandler {
	private final RabbitTemplate rabbitTemplate;

	@EventListener
    public void handle(BookingCompletedDomainEvent event) {
        Booking booking = event.getBooking();
        BookingCompletedIntegrationEvent integrationEvent = new BookingCompletedIntegrationEvent(
        		booking.getId(),
        		booking.getVehicleId(),
        		booking.getPickupDate(),
        		booking.getReturnDate());
		rabbitTemplate.convertAndSend(
			BusConstants.EXCHANGE_BOOKING,
			BusConstants.ROUTING_KEY_BOOKING_COMPLETED,
			integrationEvent
		);
    }
}
