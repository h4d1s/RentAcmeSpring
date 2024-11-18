package com.pixellabs.inventory.infrastructure.rabbitmq.eventHandling;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCanceledIntegrationEvent;

@Service
public class BookingCanceledIntegrationEventConsumer {
	
	@RabbitListener(queues = BusConstants.QUEUE_BOOKING_CANCELED)
	public void receive(BookingCanceledIntegrationEvent event) {
		System.out.println("Received <" + event.getBookingId() + ">");
	}
}
