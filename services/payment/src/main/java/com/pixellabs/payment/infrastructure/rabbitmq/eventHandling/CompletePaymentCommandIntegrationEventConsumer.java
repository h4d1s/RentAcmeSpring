package com.pixellabs.payment.infrastructure.rabbitmq.eventHandling;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.commands.CompletePaymentIntegrationCommand;
import com.pixellabs.eventbus.events.PaymentCompletedIntegrationEvent;
import com.pixellabs.eventbus.events.PaymentFailedIntegrationEvent;
import com.pixellabs.eventbus.requests.GetUserIntegrationRequest;
import com.pixellabs.eventbus.responses.GetUserIntegrationResponse;
import com.pixellabs.payment.application.models.CustomerDTO;
import com.pixellabs.payment.infrastructure.rabbitmq.IntegrationEventService;
import com.pixellabs.payment.infrastructure.services.gateway.PaymentGateway;
import com.stripe.exception.StripeException;

@Service
public class CompletePaymentCommandIntegrationEventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CompletePaymentCommandIntegrationEventConsumer.class);
	
	@Autowired
	private PaymentGateway paymentGateway;
	
	@Autowired
	private IntegrationEventService integrationEventService;
	
	@RabbitListener(queues = BusConstants.QUEUE_PAYMENT_COMPLETE_COMMAND)
	public void receive(CompletePaymentIntegrationCommand command) {
		UUID userId = command.getUserId();
        BigDecimal amount = command.getTotalPrice();
        
		try {
			CustomerDTO customer = paymentGateway.getCustomerByEmail("");
			
			GetUserIntegrationRequest request = new GetUserIntegrationRequest(userId);
	        GetUserIntegrationResponse response = (GetUserIntegrationResponse) integrationEventService.sendAndReceive(request, BusConstants.EXCHANGE_USER, BusConstants.ROUTING_KEY_USER_GET_REQUEST);
	        
	        if (customer == null) {
	            logger.error("Could not retrieve customer with email: " + response.getEmail());
	            PaymentFailedIntegrationEvent failedEvent = new PaymentFailedIntegrationEvent(
	            	userId
	            );
	            failedEvent.setCorrelateId(command.getCorrelateId());
	            integrationEventService.publishEvent(failedEvent, BusConstants.EXCHANGE_PAYMENT, BusConstants.ROUTING_KEY_PAYMENT_FAILED);
	        } else {
	    		 paymentGateway.charge(customer.getId().toString(), amount);
	    		 PaymentCompletedIntegrationEvent completedEvent = new PaymentCompletedIntegrationEvent(
	    			customer.getId()
	             );
	    		 completedEvent.setCorrelateId(command.getCorrelateId());
	    		 integrationEventService.publishEvent(completedEvent, BusConstants.EXCHANGE_PAYMENT, BusConstants.QUEUE_PAYMENT_COMPLETED);
	        }
		} catch (StripeException e) {
			PaymentFailedIntegrationEvent failedEvent = new PaymentFailedIntegrationEvent(
				userId
            );
			failedEvent.setCorrelateId(command.getCorrelateId());
	        integrationEventService.publishEvent(failedEvent, BusConstants.EXCHANGE_PAYMENT, BusConstants.ROUTING_KEY_PAYMENT_FAILED);
		}
	}

}
