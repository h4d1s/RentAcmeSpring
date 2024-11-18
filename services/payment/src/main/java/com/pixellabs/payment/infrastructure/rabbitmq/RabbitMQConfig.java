package com.pixellabs.payment.infrastructure.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.BusConstants;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

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
    public Binding paymentCompleteCommandBinding(DirectExchange paymentExchange, Queue paymentCompleteCommandQueue) {
        return BindingBuilder.bind(paymentCompleteCommandQueue)
                             .to(paymentExchange)
                             .with(BusConstants.ROUTING_KEY_PAYMENT_COMPLETE_COMMAND);
    }
    @Bean
    public Binding paymentCompletedBinding(DirectExchange paymentExchange, Queue paymentCompletedQueue) {
        return BindingBuilder.bind(paymentCompletedQueue)
                             .to(paymentExchange)
                             .with(BusConstants.ROUTING_KEY_PAYMENT_COMPLETED);
    }
    @Bean
    public Binding paymentFailedBinding(DirectExchange paymentExchange, Queue paymentFailedQueue) {
        return BindingBuilder.bind(paymentFailedQueue)
                             .to(paymentExchange)
                             .with(BusConstants.ROUTING_KEY_PAYMENT_FAILED);
    }
    
    @Bean
    public Binding userCreatedBinding(DirectExchange userExchange, Queue userCreatedQueue) {
        return BindingBuilder.bind(userCreatedQueue)
        					 .to(userExchange)
        					 .with(BusConstants.ROUTING_KEY_USER_CREATED);
    }
}
