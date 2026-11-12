package com.pixellabs.user.infrastructure.messaging;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pixellabs.eventbus.Queues;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;

@Configuration
public class RabbitMQConfig {
    @Bean(name = "rawRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rawRabbitListenerContainerFactory(
        ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
            new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(new SimpleMessageConverter());

        return factory;
    }

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
        return new DirectExchange(Exchanges.USER);
    }
    @Bean
    public TopicExchange keycloakTopicExchange() { return new TopicExchange(Exchanges.KEYCLOAK, true, false); }
	
	// Queues
	@Bean
    public Queue userCreatedQueue() {
        return new Queue(Queues.USER_CREATED, true);
    }
	@Bean
    public Queue userCheckRequestQueue() {
        return new Queue(Queues.USER_CHECK_REQUEST, true);
    }
	@Bean
    public Queue userGetRequestQueue() {
        return new Queue(Queues.USER_GET_REQUEST, true);
    }

    @Bean
    public Queue keycloakClientEventQueue() { return new Queue(Queues.KEYCLOAK_CLIENT_EVENTS, true); }

	// Bindings
    @Bean
    public Binding userCreatedRequestBinding() {
        return BindingBuilder.bind(userCreatedQueue())
                             .to(userExchange())
                             .with(RoutingKeys.USER_CREATED);
    }
    @Bean
    public Binding userCheckRequestBinding() {
        return BindingBuilder.bind(userCheckRequestQueue())
                             .to(userExchange())
                             .with(RoutingKeys.USER_CHECK_REQUEST);
    }
    @Bean
    public Binding userGetRequestBinding() {
        return BindingBuilder.bind(userGetRequestQueue())
                             .to(userExchange())
                             .with(RoutingKeys.USER_GET_REQUEST);
    }

    @Bean
    public Binding keycloakClientEventBinding() {
        return BindingBuilder
            .bind(keycloakClientEventQueue())
            .to(keycloakTopicExchange())
            .with(RoutingKeys.KEYCLOAK_CLIENT_EVENTS);
    }
    @Bean
    public Binding keycloakAdminEventBinding() {
        return BindingBuilder
            .bind(keycloakClientEventQueue())
            .to(keycloakTopicExchange())
            .with(RoutingKeys.KEYCLOAK_ADMIN_EVENTS);
    }
}
