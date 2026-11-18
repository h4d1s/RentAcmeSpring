package com.pixellabs.payment.application.event.handler;

import com.pixellabs.libs.eventbus.Exchanges;
import com.pixellabs.libs.eventbus.RoutingKeys;
import com.pixellabs.libs.eventbus.events.PaymentIntentCanceledIntegrationEvent;
import com.pixellabs.payment.domain.paymentAggregate.Payment;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentCanceledDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentIntentCanceledDomainEventHandler {
    private final RabbitTemplate rabbitTemplate;

    @EventListener
    public void handle(PaymentIntentCanceledDomainEvent event) {
        Payment payment = event.payment();
        PaymentIntentCanceledIntegrationEvent paymentIntentCanceledIntegrationEvent = new PaymentIntentCanceledIntegrationEvent(
            UUID.randomUUID(),
            payment.getId(),
            payment.getBookingId()
        );
        rabbitTemplate.convertAndSend(
            Exchanges.PAYMENT,
            RoutingKeys.PAYMENT_INTENT_CANCELED,
            paymentIntentCanceledIntegrationEvent
        );
    }
}