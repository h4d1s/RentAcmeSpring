package com.pixellabs.payment.application.event.handler;

import com.pixellabs.libs.eventbus.Exchanges;
import com.pixellabs.libs.eventbus.RoutingKeys;
import com.pixellabs.libs.eventbus.events.PaymentIntentFailedIntegrationEvent;
import com.pixellabs.payment.domain.paymentAggregate.Payment;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentFailedDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentIntentFailedDomainEventHandler {
    private final RabbitTemplate rabbitTemplate;

    @EventListener
    public void handle(PaymentIntentFailedDomainEvent event) {
        Payment payment = event.payment();
        PaymentIntentFailedIntegrationEvent paymentIntentFailedIntegrationEvent = new PaymentIntentFailedIntegrationEvent(
            UUID.randomUUID(),
            payment.getUserId()
        );
        rabbitTemplate.convertAndSend(
            Exchanges.PAYMENT,
            RoutingKeys.PAYMENT_INTENT_FAILED,
            paymentIntentFailedIntegrationEvent
        );
    }
}
