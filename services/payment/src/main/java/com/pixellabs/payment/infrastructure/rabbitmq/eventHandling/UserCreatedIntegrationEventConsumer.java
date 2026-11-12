package com.pixellabs.payment.infrastructure.rabbitmq.eventHandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.Queues;
import com.pixellabs.eventbus.events.UserCreatedIntegrationEvent;
import com.pixellabs.payment.infrastructure.services.gateway.PaymentGateway;
import com.stripe.exception.StripeException;

@Service
public class UserCreatedIntegrationEventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CompletePaymentCommandIntegrationEventConsumer.class);
	
	@Autowired
	private PaymentGateway paymentGateway;
	
	@RabbitListener(queues = Queues.USER_CREATED)
	public void receive(UserCreatedIntegrationEvent event) {
        try {
			paymentGateway.createCustomer(
				event.firstName(),
				event.lastName(),
				event.email());
			logger.error("Created customer: {}", event.email());
		} catch (StripeException e) {
			logger.error("Error creating customer: {}", event.email());
		}
	}
	
}
