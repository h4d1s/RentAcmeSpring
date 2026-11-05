package com.pixellabs.state_machine.infrastructure.rabbitmq.eventHandling;

//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.config.StateMachineFactory;
//
//import com.pixellabs.eventbus.BusConstants;
//import com.pixellabs.eventbus.events.PaymentCompletedIntegrationEvent;
//import com.pixellabs.state_machine.infrastructure.configs.Events;
//import com.pixellabs.state_machine.infrastructure.configs.States;
//
//import reactor.core.publisher.Mono;

//public class PaymentCompletedIntegrationEventConsumer {
//
//	@Autowired
//	private StateMachineFactory<States, Events> stateMachineFactory;
//
//    @RabbitListener(queues = BusConstants.QUEUE_PAYMENT_COMPLETED)
//    public void handle(PaymentCompletedIntegrationEvent event) {
//		StateMachine<States, Events> stateMachine = stateMachineFactory.getStateMachine(event.getCorrelateId().toString());
//        stateMachine.sendEvent(Mono.just(
//        		MessageBuilder.withPayload(Events.PAYMENT_COMPLETED_EVENT)
//                .setHeader("paymentCompletedEvent", event)
//                .build()))
//        	.blockLast();
//    }
//
//}
