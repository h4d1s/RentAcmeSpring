package com.pixellabs.payment.application.event.handler;

import com.pixellabs.libs.eventbus.Exchanges;
import com.pixellabs.libs.eventbus.RoutingKeys;
import com.pixellabs.libs.eventbus.events.PaymentIntentCreatedIntegrationEvent;
import com.pixellabs.payment.domain.paymentAggregate.Payment;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentCreatedDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentIntentCreatedDomainEventHandler {
    private final RabbitTemplate rabbitTemplate;

    @EventListener
    public void handle(PaymentIntentCreatedDomainEvent event) {
        Payment payment = event.payment();
        PaymentIntentCreatedIntegrationEvent paymentIntentCreatedIntegrationEvent = new PaymentIntentCreatedIntegrationEvent(
            UUID.randomUUID(),
            payment.getUserId(),
            payment.getStripePaymentIntentId()
        );
        rabbitTemplate.convertAndSend(
            Exchanges.PAYMENT,
            RoutingKeys.PAYMENT_INTENT_CREATED,
            paymentIntentCreatedIntegrationEvent
        );
    }
}
