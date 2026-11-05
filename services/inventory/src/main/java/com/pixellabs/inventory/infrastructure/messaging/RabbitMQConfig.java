package com.pixellabs.inventory.infrastructure.messaging;

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
    public DirectExchange inventoryExchange() {
        return new DirectExchange(BusConstants.EXCHANGE_INVENTORY);
    }
	
	// Queues
	@Bean
    public Queue bookingCanceledQueue() {
        return new Queue(BusConstants.QUEUE_BOOKING_CANCELED, true);
    }
	@Bean
    public Queue bookingCompletedQueue() {
        return new Queue(BusConstants.QUEUE_BOOKING_COMPLETED, true);
    }
	
	@Bean
    public Queue vehicleLockCommandQueue() {
        return new Queue(BusConstants.QUEUE_VEHICLE_LOCK_COMMAND, true);
    }
	@Bean
    public Queue vehicleUnlockCommandQueue() {
        return new Queue(BusConstants.QUEUE_VEHICLE_UNLOCK_COMMAND, true);
    }
	
	// Bindings
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
    public Binding vehicleLockCommandBinding() {
        return BindingBuilder.bind(vehicleLockCommandQueue())
                             .to(inventoryExchange())
                             .with(BusConstants.ROUTING_KEY_VEHICLE_LOCKED);
    }
    @Bean
    public Binding vehicleUnlockCommandBinding() {
        return BindingBuilder.bind(vehicleUnlockCommandQueue())
                             .to(inventoryExchange())
                             .with(BusConstants.ROUTING_KEY_VEHICLE_UNLOCKED);
    }
}