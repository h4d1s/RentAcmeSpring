package com.pixellabs.notification.infrastructure.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.BusConstants;

@Configuration
public class RabbitMQConfig {    
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter());
        return factory;
    }
    
	// Exchanges
	@Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(BusConstants.EXCHANGE_BOOKING);
    }
	
	// Queues
	@Bean
    public Queue bookingReservedQueue() {
        return new Queue(BusConstants.QUEUE_BOOKING_RESERVED, true);
    }
	@Bean
    public Queue bookingCanceledQueue() {
        return new Queue(BusConstants.QUEUE_BOOKING_CANCELED, true);
    }
	@Bean
    public Queue bookingCompletedQueue() {
        return new Queue(BusConstants.QUEUE_BOOKING_COMPLETED, true);
    }
	
	// Bindings
    @Bean
    public Binding bookingReservedBinding(DirectExchange bookingExchange, Queue bookingReservedQueue) {
        return BindingBuilder.bind(bookingReservedQueue)
                             .to(bookingExchange)
                             .with(BusConstants.ROUTING_KEY_BOOKING_RESERVED);
    }
    @Bean
    public Binding bookingCanceledBinding(DirectExchange bookingExchange, Queue bookingCanceledQueue) {
        return BindingBuilder.bind(bookingCanceledQueue)
                             .to(bookingExchange)
                             .with(BusConstants.ROUTING_KEY_BOOKING_CANCELED);
    }
    @Bean
    public Binding bookingCompletedBinding(DirectExchange bookingExchange, Queue bookingCompletedQueue) {
        return BindingBuilder.bind(bookingCompletedQueue)
                             .to(bookingExchange)
                             .with(BusConstants.ROUTING_KEY_BOOKING_COMPLETED);
    }
}
