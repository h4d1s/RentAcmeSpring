package com.pixellabs.payment.infrastructure.rabbitmq;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.BusConstants;

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
    public DirectExchange paymentExchange() {
        return new DirectExchange(BusConstants.EXCHANGE_PAYMENT);
    }
	
	@Bean
    public DirectExchange userExchange() {
        return new DirectExchange(BusConstants.EXCHANGE_USER);
    }
	
	// Queues
	@Bean
    public Queue paymentCompleteCommandQueue() {
        return new Queue(BusConstants.QUEUE_PAYMENT_COMPLETE_COMMAND, true);
    }
	@Bean
    public Queue paymentCompletedQueue() {
        return new Queue(BusConstants.QUEUE_PAYMENT_COMPLETED, true);
    }
	@Bean
    public Queue paymentFailedQueue() {
        return new Queue(BusConstants.QUEUE_PAYMENT_FAILED, true);
    }
	
	@Bean
    public Queue userCreatedQueue() {
        return new Queue(BusConstants.QUEUE_USER_CREATED, true);
    }
	
	// Bindings
    @Bean
    public Binding paymentCompleteCommandBinding() {
        return BindingBuilder.bind(paymentCompleteCommandQueue())
                             .to(paymentExchange())
                             .with(BusConstants.ROUTING_KEY_PAYMENT_COMPLETE_COMMAND);
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
    
    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder.bind(userCreatedQueue())
        					 .to(userExchange())
        					 .with(BusConstants.ROUTING_KEY_USER_CREATED);
    }
}
