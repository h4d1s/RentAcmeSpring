package com.pixellabs.reservation.infrastructure.messaging;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.Queues;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class RabbitMQConfig {
    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter jacksonJsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonJsonMessageConverter);
        return template;
    }
    
	// Exchanges
	@Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(Exchanges.BOOKING);
    }
	@Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange(Exchanges.INVENTORY);
    }
	
	// Queues
	@Bean
    public Queue bookingReservedQueue() {
        return new Queue(Queues.BOOKING_RESERVED, true);
    }
	@Bean
    public Queue bookingCanceledQueue() {
        return new Queue(Queues.BOOKING_CANCELED, true);
    }
	@Bean
    public Queue bookingCompletedQueue() {
        return new Queue(Queues.BOOKING_COMPLETED, true);
    }
	
	@Bean
    public Queue vehicleGetRequestQueue() {
        return new Queue(Queues.VEHICLE_GET_REQUEST, true);
    }
	@Bean
    public Queue vehicleCheckRequestQueue() {
        return new Queue(Queues.VEHICLE_CHECK_REQUEST, true);
    }
	
	// Bindings
    @Bean
    public Binding bookingReservedBinding() {
        return BindingBuilder.bind(bookingReservedQueue())
                             .to(bookingExchange())
                             .with(RoutingKeys.BOOKING_RESERVED);
    }
    @Bean
    public Binding bookingCanceledBinding() {
        return BindingBuilder.bind(bookingCanceledQueue())
                             .to(bookingExchange())
                             .with(RoutingKeys.BOOKING_CANCELED);
    }
    @Bean
    public Binding bookingCompletedBinding() {
        return BindingBuilder.bind(bookingCompletedQueue())
                             .to(bookingExchange())
                             .with(RoutingKeys.BOOKING_COMPLETED);
    }
    
    @Bean
    public Binding vehicleGetRequestBinding() {
        return BindingBuilder.bind(vehicleGetRequestQueue())
                             .to(inventoryExchange())
                             .with(RoutingKeys.VEHICLE_GET_REQUEST);
    }
    @Bean
    public Binding vehicleCheckRequestBinding() {
        return BindingBuilder.bind(vehicleCheckRequestQueue())
                             .to(inventoryExchange())
                             .with(RoutingKeys.VEHICLE_CHECK_REQUEST);
    }
}
