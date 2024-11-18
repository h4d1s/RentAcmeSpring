package com.pixellabs.state_machine.infrastructure.configs;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import com.pixellabs.eventbus.commands.CompletePaymentIntegrationCommand;
import com.pixellabs.eventbus.events.BookingCompletedIntegrationEvent;
import com.pixellabs.eventbus.events.BookingFailedIntegrationEvent;
import com.pixellabs.eventbus.events.BookingReservedIntegrationEvent;
import com.pixellabs.eventbus.events.PaymentCompletedIntegrationEvent;
import com.pixellabs.eventbus.events.PaymentFailedIntegrationEvent;
import com.pixellabs.state_machine.infrastructure.services.BookingService;
import com.pixellabs.state_machine.infrastructure.services.PaymentService;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig
	extends EnumStateMachineConfigurerAdapter<States, Events> {

	private static final Logger logger = LoggerFactory.getLogger(StateMachineConfig.class);
	
	@Autowired
	private StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
    		throws Exception {
        states
            .withStates()
            .initial(States.INITIAL)
            .states(EnumSet.allOf(States.class))
            .end(States.PAYMENT_COMPLETED)
            .end(States.PAYMENT_FAILED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
    		throws Exception {
    	config
			.withConfiguration()
				.autoStartup(true);
    	//config
    	//	.withPersistence()
    	//		.runtimePersister(stateMachineRuntimePersister);
    }
    
    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {    	
    	transitions
	        .withExternal()
	            .source(States.INITIAL).target(States.RESERVED).event(Events.BOOKING_RESERVED_EVENT)
	            .action(context -> {
	                logger.info("BookingReservedEvent received in state machine");
	                BookingReservedIntegrationEvent bookingReservedEvent = context.getMessageHeaders().get("bookingReservedEvent", BookingReservedIntegrationEvent.class);

	                context.getExtendedState().getVariables().put("bookingReservedEvent", bookingReservedEvent);
	                
	                CompletePaymentIntegrationCommand command = new CompletePaymentIntegrationCommand(
	                	bookingReservedEvent.getUserId(),
	                	bookingReservedEvent.getPrice());
	                command.setCorrelateId(bookingReservedEvent.getCorrelateId());
	                paymentService.send(command);
	                
	                logger.info("CompletePaymentCommand sent in state machine");
	            })
	            .and()
	        .withExternal()
	            .source(States.RESERVED).target(States.PAYMENT_COMPLETED).event(Events.PAYMENT_COMPLETED_EVENT)
	            .action(context -> {
	                logger.info("PaymentCompletedEvent received in state machine");
	                PaymentCompletedIntegrationEvent paymentCompletedEvent = context.getMessageHeaders().get("paymentCompletedEvent", PaymentCompletedIntegrationEvent.class);
	                
	                BookingReservedIntegrationEvent bookingReservedEvent = (BookingReservedIntegrationEvent) context.getExtendedState().getVariables().get("bookingReservedEvent");
	                BookingCompletedIntegrationEvent bookingCompletedEvent = new BookingCompletedIntegrationEvent(
	            		bookingReservedEvent.getBookingId(),
	            		bookingReservedEvent.getVehicleId(),
	            		bookingReservedEvent.getPickupDate(),
	            		bookingReservedEvent.getReturnDate());
	                bookingService.send(bookingCompletedEvent);
	                
	                logger.info("BookingCompletedIntegrationEvent sent in state machine");
	            })
	            .and()
	        .withExternal()
	            .source(States.RESERVED).target(States.PAYMENT_FAILED).event(Events.PAYMENT_FAILED_EVENT)
	            .action(context -> {
	                logger.info("PaymentFailedEvent received in state machine");
	                PaymentFailedIntegrationEvent paymentFailedEvent = context.getMessageHeaders().get("paymentFailedEvent", PaymentFailedIntegrationEvent.class);
	                
	                BookingReservedIntegrationEvent bookingReservedEvent = (BookingReservedIntegrationEvent) context.getExtendedState().getVariables().get("bookingReservedEvent");
	                BookingFailedIntegrationEvent event = new BookingFailedIntegrationEvent(bookingReservedEvent.getBookingId());
	                bookingService.send(event);
	                
	                logger.info("BookingFailedIntegrationEvent sent in state machine");
	            });
    }
}