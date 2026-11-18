package com.pixellabs.payment.infrastructure.messaging.listeners;

import com.pixellabs.payment.infrastructure.payment.services.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.pixellabs.libs.eventbus.Queues;
import com.pixellabs.libs.eventbus.events.UserCreatedIntegrationEvent;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreatedIntegrationEventListener {
	private final StripePaymentService paymentService;
    private final Validator validator;
	
	@RabbitListener(queues = Queues.USER_CREATED)
	public void handle(UserCreatedIntegrationEvent event) throws StripeException {
        Set<ConstraintViolation<UserCreatedIntegrationEvent>> violations =
            validator.validate(event);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Customer customer = paymentService.createCustomer(
            event.id(),
            event.firstName(),
            event.lastName(),
            event.email());
        log.info("Created customer: {}", customer.getEmail());
    }
}
