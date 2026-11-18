package com.pixellabs.payment.infrastructure.messaging.listeners;

import java.util.Set;
import java.util.stream.Collectors;

import com.pixellabs.libs.eventbus.commands.CreatePaymentIntentCommand;
import com.pixellabs.libs.user.grpc.UserGrpcClient;
import com.pixellabs.libs.user.grpc.models.UserResponse;
import com.pixellabs.payment.domain.exceptions.PaymentDomainException;
import com.pixellabs.payment.domain.paymentAggregate.Payment;
import com.pixellabs.payment.domain.paymentAggregate.PaymentRepository;
import com.pixellabs.payment.infrastructure.messaging.exceptions.CustomerNotFoundException;
import com.pixellabs.payment.infrastructure.messaging.exceptions.UserNotFoundException;
import com.pixellabs.payment.infrastructure.payment.services.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.pixellabs.libs.eventbus.Queues;

@Service
@RequiredArgsConstructor
public class CreatePaymentIntentCommandListener {
	private final StripePaymentService stripePaymentService;
	private final UserGrpcClient userGrpcClient;
	private final Validator validator;
	private final PaymentRepository paymentRepository;

	@RabbitListener(queues = Queues.PAYMENT_CREATE_INTENT_COMMAND)
	public void handle(CreatePaymentIntentCommand command)
        throws PaymentDomainException, StripeException {
		Set<ConstraintViolation<CreatePaymentIntentCommand>> violations =
			validator.validate(command);
		if (!violations.isEmpty()) {
			String errors = violations.stream()
				.map(v -> v.getPropertyPath() + ": " + v.getMessage())
				.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(errors);
		}

		UserResponse user = userGrpcClient.getUser(command.userId())
			.orElseThrow(() -> new UserNotFoundException("User not found."));
		Customer customer = stripePaymentService.getCustomerByEmail(user.email())
			.orElseThrow(() -> new CustomerNotFoundException("Customer not found."));
		PaymentIntent paymentIntent = stripePaymentService.createIntent(
			command.correlateId(),
			user.id(),
			command.totalPrice(),
			customer.getId()
		);

		Payment payment = new Payment(
			command.correlateId(),
			command.userId(),
			paymentIntent.getId(),
			paymentIntent.getAmount(),
			paymentIntent.getCurrency());
		paymentRepository.save(payment);
    }
}
