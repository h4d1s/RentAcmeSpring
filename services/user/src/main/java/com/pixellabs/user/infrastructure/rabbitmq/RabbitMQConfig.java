package com.pixellabs.user.infrastructure.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.BusConstants;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter jacksonJsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonJsonMessageConverter);
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
    public Binding userCreatedRequestBinding() {
        return BindingBuilder.bind(userCreatedQueue())
                             .to(userExchange())
                             .with(BusConstants.ROUTING_KEY_USER_CREATED);
    }
    @Bean
    public Binding userCheckRequestBinding() {
        return BindingBuilder.bind(userCheckRequestQueue())
                             .to(userExchange())
                             .with(BusConstants.ROUTING_KEY_USER_CHECK_REQUEST);
    }
    @Bean
    public Binding userGetRequestBinding() {
        return BindingBuilder.bind(userGetRequestQueue())
                             .to(userExchange())
                             .with(BusConstants.ROUTING_KEY_USER_GET_REQUEST);
    }
}
