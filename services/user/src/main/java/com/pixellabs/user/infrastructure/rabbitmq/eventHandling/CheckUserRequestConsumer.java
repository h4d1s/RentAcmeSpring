package com.pixellabs.user.infrastructure.rabbitmq.eventHandling;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.requests.CheckUserIntegrationRequest;
import com.pixellabs.eventbus.responses.UserStatusIntegrationResponse;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import com.pixellabs.user.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class CheckUserRequestConsumer {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	IntegrationEventService integrationService;
	
	@RabbitListener(queues = BusConstants.QUEUE_USER_CHECK_REQUEST)
	public void receive(CheckUserIntegrationRequest request) {
        boolean isExists = userRepository.existsById(request.getId());
        UserStatusIntegrationResponse response = new UserStatusIntegrationResponse(isExists);
        integrationService.publishEvent(response, BusConstants.EXCHANGE_USER, BusConstants.ROUTING_KEY_USER_CHECK_REQUEST);
	}
}
