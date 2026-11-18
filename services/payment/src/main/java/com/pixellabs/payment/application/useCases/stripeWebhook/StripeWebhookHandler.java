package com.pixellabs.payment.application.useCases.stripeWebhook;

import com.pixellabs.payment.application.exceptions.NotFoundException;
import com.pixellabs.payment.domain.exceptions.PaymentDomainException;
import com.pixellabs.payment.domain.paymentAggregate.Payment;
import com.pixellabs.payment.domain.paymentAggregate.PaymentRepository;
import com.pixellabs.payment.infrastructure.payment.configs.StripeProperties;
import com.pixellabs.payment.infrastructure.messaging.exceptions.InvalidStripeWebhookException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StripeWebhookHandler {
    private final StripeProperties stripeProperties;
    private final PaymentRepository paymentRepository;

    public void handle(StripeWebhookCommand command)
        throws StripeException, PaymentDomainException {
        Event stripeEvent = Webhook.constructEvent(
            command.json(),
            command.stripeSignature(),
            stripeProperties.webhook().secret()
        );
        switch (stripeEvent.getType()) {
            case "payment_intent.succeeded" -> {
                PaymentIntent paymentIntent = getPaymentIntent(stripeEvent);
                Payment payment = getPayment(paymentIntent);
                payment.markAsSucceeded();
                paymentRepository.save(payment);
            }
            case "payment_intent.canceled" -> {
                PaymentIntent paymentIntent = getPaymentIntent(stripeEvent);
                Payment payment = getPayment(paymentIntent);
                payment.markAsCanceled();
                paymentRepository.save(payment);
            }
            case "payment_intent.payment_failed" -> {
                PaymentIntent paymentIntent = getPaymentIntent(stripeEvent);
                Payment payment = getPayment(paymentIntent);
                payment.markAsFailed();
                paymentRepository.save(payment);
            }
        }
    }

    private PaymentIntent getPaymentIntent(Event event) {
        Optional<StripeObject> stripeObject =  event
            .getDataObjectDeserializer()
            .getObject();

        return stripeObject
            .filter(PaymentIntent.class::isInstance)
            .map(PaymentIntent.class::cast)
            .orElseThrow(() -> new InvalidStripeWebhookException(
                "Unable to deserialize Stripe event: " + event.getType()
            ));
    }

    private Payment getPayment(PaymentIntent paymentIntent) {
        return paymentRepository
            .findByStripePaymentIntentId(paymentIntent.getId())
            .orElseThrow(() -> new NotFoundException(
                "Payment not found: " + paymentIntent.getId()
            ));
    }
}
