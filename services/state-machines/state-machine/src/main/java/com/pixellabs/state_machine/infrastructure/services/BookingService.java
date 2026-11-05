package com.pixellabs.state_machine.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCompletedIntegrationEvent;
import com.pixellabs.eventbus.events.BookingFailedIntegrationEvent;
import com.pixellabs.state_machine.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class BookingService {	
	@Autowired
    private IntegrationEventService integrationEventService;
	
	public void send(BookingCompletedIntegrationEvent event) {
		integrationEventService.publishEvent(event, BusConstants.EXCHANGE_BOOKING, BusConstants.QUEUE_BOOKING_COMPLETED);
	}

	public void send(BookingFailedIntegrationEvent event) {
		integrationEventService.publishEvent(event, BusConstants.EXCHANGE_BOOKING, BusConstants.QUEUE_BOOKING_FAILED);
	}
}
