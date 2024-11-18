package com.pixellabs.state_machine.infrastructure.rabbitmq.eventHandling;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BaseIntegrationEvent;
import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingReservedIntegrationEvent;
import com.pixellabs.state_machine.infrastructure.configs.Events;
import com.pixellabs.state_machine.infrastructure.configs.States;

import reactor.core.publisher.Mono;

@Service
public class BookingReservedIntegrationEventConsumer {
	
	@Autowired
	private StateMachineFactory<States, Events> stateMachineFactory;
	
	@RabbitListener(queues = BusConstants.QUEUE_BOOKING_RESERVED)
	public void handle(BookingReservedIntegrationEvent event) {
		System.out.println(">>RESERVED " + event.getCorrelateId());
		StateMachine<States, Events> stateMachine = stateMachineFactory.getStateMachine(event.getCorrelateId().toString());
        stateMachine.sendEvent(Mono.just(
        		MessageBuilder.withPayload(Events.BOOKING_RESERVED_EVENT)
                .setHeader("bookingReservedEvent", event)
                .build()))
        		.blockLast();
	}
}
