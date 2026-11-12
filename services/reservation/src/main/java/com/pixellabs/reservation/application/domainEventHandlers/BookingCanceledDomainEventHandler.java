package com.pixellabs.reservation.application.domainEventHandlers;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.events.BookingCanceledIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCanceledDomainEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingCanceledDomainEventHandler {
    private final RabbitTemplate rabbitTemplate;

	@EventListener
    public void handle(BookingCanceledDomainEvent event) {
        Booking booking = event.booking();
        BookingCanceledIntegrationEvent bookingCanceledEvent = new BookingCanceledIntegrationEvent(
            UUID.randomUUID(),
            booking.getId(),
            booking.getVehicleId());
        rabbitTemplate.convertAndSend(
            Exchanges.BOOKING,
            RoutingKeys.BOOKING_CANCELED,
            bookingCanceledEvent
        );
    }
}
