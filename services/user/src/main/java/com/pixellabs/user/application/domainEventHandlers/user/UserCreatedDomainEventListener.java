package com.pixellabs.user.application.domainEventHandlers.user;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import com.pixellabs.user.application.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.events.UserCreatedIntegrationEvent;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import com.pixellabs.user.domain.userAggregate.events.UserCreatedDomainEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserCreatedDomainEventListener {
	private final UserRepository userRepository;
	private final RabbitTemplate rabbitTemplate;
	
	@EventListener
	public void handle(UserCreatedDomainEvent event)  {
		User user = userRepository.findById(event.id())
			.orElseThrow(() -> new NotFoundException("User with id " + event.id()  + "not found."));
        UserCreatedIntegrationEvent integrationEvent = new UserCreatedIntegrationEvent(
			UUID.randomUUID(),
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName());

        rabbitTemplate.convertAndSend(
            Exchanges.USER,
            RoutingKeys.USER_CREATED,
            integrationEvent
        );
	}
}
