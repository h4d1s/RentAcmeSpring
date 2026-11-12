package com.pixellabs.state_machine.infrastructure.services;

import com.pixellabs.eventbus.Exchanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.Queues;
import com.pixellabs.eventbus.commands.CompletePaymentIntegrationCommand;
import com.pixellabs.eventbus.events.PaymentFailedIntegrationEvent;
import com.pixellabs.state_machine.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class PaymentService {
	@Autowired
    private IntegrationEventService integrationEventService;
	
	public void send(CompletePaymentIntegrationCommand command) {
		integrationEventService.publishEvent(command, Exchanges.PAYMENT, Queues.PAYMENT_COMPLETE_COMMAND);
	}
	
	public void send(PaymentFailedIntegrationEvent event) {
		integrationEventService.publishEvent(event, Exchanges.PAYMENT, Queues.PAYMENT_FAILED);
	}
}
