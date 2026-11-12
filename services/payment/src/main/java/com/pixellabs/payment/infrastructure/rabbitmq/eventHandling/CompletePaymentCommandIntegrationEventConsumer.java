package com.pixellabs.payment.infrastructure.rabbitmq.eventHandling;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.Queues;
import com.pixellabs.eventbus.commands.CompletePaymentIntegrationCommand;
import com.pixellabs.eventbus.events.PaymentCompletedIntegrationEvent;
import com.pixellabs.eventbus.events.PaymentFailedIntegrationEvent;
import com.pixellabs.eventbus.requests.GetUserIntegrationRequest;
import com.pixellabs.eventbus.responses.GetUserIntegrationResponse;
import com.pixellabs.payment.application.models.CustomerDTO;
import com.pixellabs.payment.infrastructure.rabbitmq.IntegrationEventService;
import com.pixellabs.payment.infrastructure.services.gateway.PaymentGateway;
import com.stripe.exception.StripeException;

@Slf4j
@Service
public class CompletePaymentCommandIntegrationEventConsumer {
	@Autowired
	private PaymentGateway paymentGateway;
	
	@Autowired
	private IntegrationEventService integrationEventService;
	
	@RabbitListener(queues = Queues.PAYMENT_COMPLETE_COMMAND)
	public void receive(CompletePaymentIntegrationCommand command) {
		UUID userId = command.userId();
        BigDecimal amount = command.totalPrice();
        
		try {
			CustomerDTO customer = paymentGateway.getCustomerByEmail("");
			GetUserIntegrationRequest request = new GetUserIntegrationRequest(command.correlateId(), userId);
	        GetUserIntegrationResponse response = (GetUserIntegrationResponse) integrationEventService.sendAndReceive(
				request,
				Exchanges.USER,
				RoutingKeys.USER_GET_REQUEST);
	        
	        if (customer == null) {
	            log.error("Could not retrieve customer with email: " + response.email());
	            PaymentFailedIntegrationEvent failedEvent = new PaymentFailedIntegrationEvent(
					command.correlateId(),
	            	userId
	            );
	            integrationEventService.publishEvent(
					failedEvent,
					Exchanges.PAYMENT,
					RoutingKeys.PAYMENT_FAILED);
	        } else {
	    		 paymentGateway.charge(customer.getId(), amount);
	    		 PaymentCompletedIntegrationEvent completedEvent = new PaymentCompletedIntegrationEvent(
					 command.correlateId(),
					 customer.getId()
	             );
	    		 integrationEventService.publishEvent(
					 completedEvent,
					 Exchanges.PAYMENT,
					 Queues.PAYMENT_COMPLETED);
	        }
		} catch (StripeException e) {
			PaymentFailedIntegrationEvent failedEvent = new PaymentFailedIntegrationEvent(
				command.correlateId(),
				userId
            );
	        integrationEventService.publishEvent(
				failedEvent,
				Exchanges.PAYMENT,
				RoutingKeys.PAYMENT_FAILED);
		}
	}

}
