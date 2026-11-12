package com.pixellabs.reservation.application.domainEventHandlers;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.events.BookingCompletedIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCompletedDomainEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingCompletedDomainEventHandler {
	private final RabbitTemplate rabbitTemplate;

	@EventListener
    public void handle(BookingCompletedDomainEvent event) {
        Booking booking = event.booking();
        BookingCompletedIntegrationEvent integrationEvent = new BookingCompletedIntegrationEvent(
			UUID.randomUUID(),
        	booking.getId(),
        	booking.getVehicleId(),
        	booking.getPickupDate(),
        	booking.getReturnDate());
		rabbitTemplate.convertAndSend(
			Exchanges.BOOKING,
			RoutingKeys.BOOKING_COMPLETED,
			integrationEvent
		);
    }
}
