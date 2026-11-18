package com.pixellabs.user.infrastructure.messaging.listeners;

import com.pixellabs.libs.eventbus.Exchanges;
import com.pixellabs.libs.eventbus.RoutingKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.pixellabs.libs.eventbus.Queues;
import com.pixellabs.libs.eventbus.requests.CheckUserIntegrationRequest;
import com.pixellabs.libs.eventbus.responses.UserStatusIntegrationResponse;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CheckUserRequestListener {
	private final UserRepository userRepository;
	private final RabbitTemplate rabbitTemplate;

	@Transactional
	@RabbitListener(queues = Queues.USER_CHECK_REQUEST)
	public void handle(CheckUserIntegrationRequest request) {
        boolean exists = userRepository.existsById(request.id());
        UserStatusIntegrationResponse response = new UserStatusIntegrationResponse(
			request.correlateId(),
			exists);
		rabbitTemplate.convertAndSend(
			Exchanges.USER,
			RoutingKeys.USER_CHECK_REQUEST,
			response
		);
	}
}
