package com.pixellabs.inventory.infrastructure.rabbitmq.eventHandling;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCompletedIntegrationEvent;

@Service
public class BookingReservedIntegrationEventConsumer {
	
	@RabbitListener(queues = BusConstants.QUEUE_BOOKING_COMPLETED)
	public void receive(BookingCompletedIntegrationEvent event) {
		System.out.println("Received <" + event.getBookingId() + ">");
	}
}
