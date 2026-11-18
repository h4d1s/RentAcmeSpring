package com.pixellabs.user.application.domainEventHandlers.user;

import com.pixellabs.libs.eventbus.Exchanges;
import com.pixellabs.libs.eventbus.RoutingKeys;
import com.pixellabs.user.application.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.pixellabs.libs.eventbus.events.UserCreatedIntegrationEvent;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import com.pixellabs.user.domain.userAggregate.events.UserCreatedDomainEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserCreatedDomainEventListener {
	private final UserRepository userRepository;
	private final RabbitTemplate rabbitTemplate;

	@TransactionalEventListener(
		phase = TransactionPhase.AFTER_COMMIT
	)
	public void handle(UserCreatedDomainEvent event)  {
		User user = userRepository.findById(event.id())
			.orElseThrow(() -> new NotFoundException("User with id:" + event.id()  + ", not found."));
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
