package com.pixellabs.state_machine.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.commands.CompletePaymentIntegrationCommand;
import com.pixellabs.eventbus.events.PaymentFailedIntegrationEvent;
import com.pixellabs.state_machine.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class PaymentService {
	@Autowired
    private IntegrationEventService integrationEventService;
	
	public void send(CompletePaymentIntegrationCommand command) {
		integrationEventService.publishEvent(command, BusConstants.EXCHANGE_PAYMENT, BusConstants.QUEUE_PAYMENT_COMPLETE_COMMAND);
	}
	
	public void send(PaymentFailedIntegrationEvent event) {
		integrationEventService.publishEvent(event, BusConstants.EXCHANGE_PAYMENT, BusConstants.QUEUE_PAYMENT_FAILED);
	}
}
