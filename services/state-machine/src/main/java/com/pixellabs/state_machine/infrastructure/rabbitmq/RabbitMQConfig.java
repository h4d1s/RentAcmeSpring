package com.pixellabs.state_machine.infrastructure.rabbitmq;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.Queues;

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
        return new DirectExchange(Exchanges.BOOKING);
    }
	
	@Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(Exchanges.PAYMENT);
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
    public Queue paymentCompletedQueue() {
        return new Queue(Queues.PAYMENT_COMPLETED, true);
    }
	@Bean
    public Queue paymentFailedQueue() {
        return new Queue(Queues.PAYMENT_FAILED, true);
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
    public Binding paymentCompletedBinding() {
        return BindingBuilder.bind(paymentCompletedQueue())
                             .to(paymentExchange())
                             .with(RoutingKeys.PAYMENT_COMPLETED);
    }
    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder.bind(paymentFailedQueue())
                             .to(paymentExchange())
                             .with(RoutingKeys.PAYMENT_FAILED);
    }
}
