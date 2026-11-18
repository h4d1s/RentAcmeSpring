package com.pixellabs.payment.application.event.handler;

import com.pixellabs.libs.eventbus.Exchanges;
import com.pixellabs.libs.eventbus.RoutingKeys;
import com.pixellabs.libs.eventbus.events.PaymentIntentSucceededIntegrationEvent;
import com.pixellabs.payment.domain.paymentAggregate.Payment;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentSucceededDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentIntentSucceededDomainEventHandler {
    private final RabbitTemplate rabbitTemplate;

    @EventListener
    public void handle(PaymentIntentSucceededDomainEvent event) {
        Payment payment = event.payment();
        PaymentIntentSucceededIntegrationEvent paymentIntentSucceededIntegrationEvent = new PaymentIntentSucceededIntegrationEvent(
            UUID.randomUUID(),
            payment.getUserId()
        );
        rabbitTemplate.convertAndSend(
            Exchanges.PAYMENT,
            RoutingKeys.PAYMENT_INTENT_SUCCEEDED,
            paymentIntentSucceededIntegrationEvent
        );
    }
}
