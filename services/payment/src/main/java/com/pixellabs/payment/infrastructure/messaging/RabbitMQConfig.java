package com.pixellabs.payment.infrastructure.messaging;

import com.pixellabs.libs.eventbus.Exchanges;
import com.pixellabs.libs.eventbus.RoutingKeys;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.libs.eventbus.Queues;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;

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

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        ConnectionFactory connectionFactory,
        JacksonJsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory =
            new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(
            RetryInterceptorBuilder.stateless()
                .maxRetries(3)
                .backOffOptions(1000, 2.0, 10000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build()
        );
        return factory;
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
    public Queue paymentCreateIntentCommandQueue() {
        return new Queue(Queues.PAYMENT_CREATE_INTENT_COMMAND, true);
    }
    @Bean
    public Queue paymentIntentCreatedQueue() { return new Queue(Queues.PAYMENT_CREATED, true); }
	@Bean
    public Queue paymentSucceededQueue() { return new Queue(Queues.PAYMENT_SUCCEEDED, true); }
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
    public Binding paymentCreateIntentCommandBinding() {
        return BindingBuilder.bind(paymentCreateIntentCommandQueue())
            .to(paymentExchange())
            .with(RoutingKeys.PAYMENT_INTENT_CREATE);
    }
    @Bean
    public Binding paymentIntentCreatedBinding() {
        return BindingBuilder.bind(paymentIntentCreatedQueue())
            .to(paymentExchange())
            .with(RoutingKeys.PAYMENT_INTENT_CREATED);
    }
    @Bean
    public Binding paymentSucceededBinding() {
        return BindingBuilder.bind(paymentSucceededQueue())
            .to(paymentExchange())
            .with(RoutingKeys.PAYMENT_INTENT_SUCCEEDED);
    }
    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder.bind(paymentFailedQueue())
            .to(paymentExchange())
            .with(RoutingKeys.PAYMENT_INTENT_FAILED);
    }
    
    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder.bind(userCreatedQueue())
        	.to(userExchange())
        	.with(RoutingKeys.USER_CREATED);
    }
}
