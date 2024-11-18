package com.pixellabs.state_machine.infrastructure.rabbitmq.eventHandling;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.PaymentFailedIntegrationEvent;
import com.pixellabs.state_machine.infrastructure.configs.Events;
import com.pixellabs.state_machine.infrastructure.configs.States;

import reactor.core.publisher.Mono;

public class PaymentFailedIntegrationEventConsumer {

	@Autowired
	private StateMachineFactory<States, Events> stateMachineFactory;
    
    @RabbitListener(queues = BusConstants.QUEUE_PAYMENT_FAILED)
    public void handleBookingReserved(PaymentFailedIntegrationEvent event) {
		StateMachine<States, Events> stateMachine = stateMachineFactory.getStateMachine(event.getCorrelateId().toString());
        stateMachine.sendEvent(Mono.just(
        		MessageBuilder.withPayload(Events.PAYMENT_FAILED_EVENT)
                .setHeader("paymentFailedEvent", event)
                .build()))
        	.blockLast();
    }

}
