package com.pixellabs.user.infrastructure.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingReservedIntegrationEvent;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class RabbitMQConfig {    
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    
	// Exchanges
	@Bean
    public DirectExchange userExchange() {
        return new DirectExchange(BusConstants.EXCHANGE_USER);
    }
	
	// Queues
	@Bean
    public Queue userCreatedQueue() {
        return new Queue(BusConstants.QUEUE_USER_CREATED, true);
    }
	@Bean
    public Queue userCheckRequestQueue() {
        return new Queue(BusConstants.QUEUE_USER_CHECK_REQUEST, true);
    }
	@Bean
    public Queue userGetRequestQueue() {
        return new Queue(BusConstants.QUEUE_USER_GET_REQUEST, true);
    }
	
	// Bindings
    @Bean
    public Binding userCreatedRequestBinding(DirectExchange userExchange, Queue userCreatedQueue) {
        return BindingBuilder.bind(userCreatedQueue)
                             .to(userExchange)
                             .with(BusConstants.ROUTING_KEY_USER_CREATED);
    }
    @Bean
    public Binding userCheckRequestBinding(DirectExchange userExchange, Queue userCheckRequestQueue) {
        return BindingBuilder.bind(userCheckRequestQueue)
                             .to(userExchange)
                             .with(BusConstants.ROUTING_KEY_USER_CHECK_REQUEST);
    }
    @Bean
    public Binding userGetRequestBinding(DirectExchange userExchange, Queue userGetRequestQueue) {
        return BindingBuilder.bind(userGetRequestQueue)
                             .to(userExchange)
                             .with(BusConstants.ROUTING_KEY_USER_GET_REQUEST);
    }
}
