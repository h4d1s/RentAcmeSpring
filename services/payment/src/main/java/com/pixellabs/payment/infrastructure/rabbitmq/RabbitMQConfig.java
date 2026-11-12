package com.pixellabs.payment.infrastructure.rabbitmq;

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
    public DirectExchange paymentExchange() {
        return new DirectExchange(Exchanges.PAYMENT);
    }
	
	@Bean
    public DirectExchange userExchange() {
        return new DirectExchange(Exchanges.USER);
    }
	
	// Queues
	@Bean
    public Queue paymentCompleteCommandQueue() {
        return new Queue(Queues.PAYMENT_COMPLETE_COMMAND, true);
    }
	@Bean
    public Queue paymentCompletedQueue() {
        return new Queue(Queues.PAYMENT_COMPLETED, true);
    }
	@Bean
    public Queue paymentFailedQueue() {
        return new Queue(Queues.PAYMENT_FAILED, true);
    }
	
	@Bean
    public Queue userCreatedQueue() {
        return new Queue(Queues.USER_CREATED, true);
    }
	
	// Bindings
    @Bean
    public Binding paymentCompleteCommandBinding() {
        return BindingBuilder.bind(paymentCompleteCommandQueue())
                             .to(paymentExchange())
                             .with(RoutingKeys.PAYMENT_COMPLETE_COMMAND);
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
    
    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder.bind(userCreatedQueue())
        					 .to(userExchange())
        					 .with(RoutingKeys.USER_CREATED);
    }
}
