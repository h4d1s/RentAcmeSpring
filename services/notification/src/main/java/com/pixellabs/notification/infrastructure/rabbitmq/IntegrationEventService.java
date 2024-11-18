package com.pixellabs.notification.infrastructure.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BaseIntegrationEvent;

@Service
public class IntegrationEventService {
	private final RabbitTemplate rabbitTemplate;
	
	public IntegrationEventService(RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void publishEvent(BaseIntegrationEvent event, String exchange, String routingKey) {
		rabbitTemplate.convertAndSend(exchange, routingKey, event);
	}
	
	public Object sendAndReceive(BaseIntegrationEvent event, String exchange, String routingKey) {
		return rabbitTemplate.convertSendAndReceive(exchange, routingKey, event);
	}
}