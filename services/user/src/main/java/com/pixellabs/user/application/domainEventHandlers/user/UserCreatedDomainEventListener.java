package com.pixellabs.user.application.domainEventHandlers.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.UserCreatedIntegrationEvent;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import com.pixellabs.user.domain.userAggregate.events.UserCreatedDomainEvent;
import com.pixellabs.user.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class UserCreatedDomainEventListener {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	IntegrationEventService integrationService;
	
	@EventListener
	public void handleVehicleCreatedEvent(UserCreatedDomainEvent event)
	{
		Optional<User> user = userRepository.findById(event.getId());
		if(user.isPresent()) {
			UserCreatedIntegrationEvent integrationEvent = new UserCreatedIntegrationEvent(
					user.get().getId(), user.get().getEmail(), user.get().getFirstName(), user.get().getLastName());
			integrationService.publishEvent(integrationEvent, BusConstants.EXCHANGE_USER, BusConstants.ROUTING_KEY_USER_CREATED);
		}
	}
}
