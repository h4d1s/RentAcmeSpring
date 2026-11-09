package com.pixellabs.reservation.application.domainEventHandlers;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCanceledIntegrationEvent;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCanceledDomainEvent;

@Component
@RequiredArgsConstructor
public class BookingCanceledDomainEventHandler {
    private final RabbitTemplate rabbitTemplate;

	@EventListener
    public void handle(BookingCanceledDomainEvent event) {
        Booking booking = event.getBooking();
        BookingCanceledIntegrationEvent bookingCanceledEvent = new BookingCanceledIntegrationEvent(
            booking.getId(),
            booking.getVehicleId());
        rabbitTemplate.convertAndSend(
            BusConstants.EXCHANGE_BOOKING,
            BusConstants.ROUTING_KEY_BOOKING_CANCELED,
            bookingCanceledEvent
        );
    }
}
