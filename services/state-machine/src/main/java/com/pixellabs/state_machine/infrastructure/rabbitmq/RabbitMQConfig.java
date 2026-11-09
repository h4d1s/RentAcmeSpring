package com.pixellabs.state_machine.infrastructure.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.BusConstants;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class RabbitMQConfig {

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
    
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJsonMessageConverter());
        return factory;
    }
    
	// Exchanges
	@Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(BusConstants.EXCHANGE_BOOKING);
    }
	
	@Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(BusConstants.EXCHANGE_PAYMENT);
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
	
	@Bean
    public Queue paymentCompletedQueue() {
        return new Queue(BusConstants.QUEUE_PAYMENT_COMPLETED, true);
    }
	@Bean
    public Queue paymentFailedQueue() {
        return new Queue(BusConstants.QUEUE_PAYMENT_FAILED, true);
    }
	
	// Bindings
    @Bean
    public Binding bookingReservedBinding() {
        return BindingBuilder.bind(bookingReservedQueue())
                             .to(bookingExchange())
                             .with(BusConstants.ROUTING_KEY_BOOKING_RESERVED);
    }
    @Bean
    public Binding bookingCanceledBinding() {
        return BindingBuilder.bind(bookingCanceledQueue())
                             .to(bookingExchange())
                             .with(BusConstants.ROUTING_KEY_BOOKING_CANCELED);
    }
    @Bean
    public Binding bookingCompletedBinding() {
        return BindingBuilder.bind(bookingCompletedQueue())
                             .to(bookingExchange())
                             .with(BusConstants.ROUTING_KEY_BOOKING_COMPLETED);
    }
    
    @Bean
    public Binding paymentCompletedBinding() {
        return BindingBuilder.bind(paymentCompletedQueue())
                             .to(paymentExchange())
                             .with(BusConstants.ROUTING_KEY_PAYMENT_COMPLETED);
    }
    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder.bind(paymentFailedQueue())
                             .to(paymentExchange())
                             .with(BusConstants.ROUTING_KEY_PAYMENT_FAILED);
    }
}
