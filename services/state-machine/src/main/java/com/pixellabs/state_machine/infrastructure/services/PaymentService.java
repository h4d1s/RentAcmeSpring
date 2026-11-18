package com.pixellabs.state_machine.infrastructure.services;

import com.pixellabs.libs.eventbus.Exchanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.libs.eventbus.Queues;
import com.pixellabs.libs.eventbus.commands.CompletePaymentIntegrationCommand;
import com.pixellabs.libs.eventbus.events.PaymentIntentFailedIntegrationEvent;
import com.pixellabs.state_machine.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class PaymentService {
	@Autowired
    private IntegrationEventService integrationEventService;
	
	public void send(CompletePaymentIntegrationCommand command) {
		integrationEventService.publishEvent(command, Exchanges.PAYMENT, Queues.PAYMENT_COMPLETE_COMMAND);
	}
	
	public void send(PaymentIntentFailedIntegrationEvent event) {
		integrationEventService.publishEvent(event, Exchanges.PAYMENT, Queues.PAYMENT_FAILED);
	}
}
