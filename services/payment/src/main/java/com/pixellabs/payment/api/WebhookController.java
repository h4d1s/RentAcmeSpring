package com.pixellabs.payment.api;

import com.pixellabs.payment.application.useCases.stripeWebhook.StripeWebhookCommand;
import com.pixellabs.payment.application.useCases.stripeWebhook.StripeWebhookHandler;
import com.pixellabs.payment.domain.exceptions.PaymentDomainException;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {
    private final StripeWebhookHandler webhookHandler;

    @PostMapping("/stripe")
    public ResponseEntity<Void> post(
        @RequestBody String json,
        @RequestHeader("Stripe-Signature") String stripeSignature)
        throws StripeException, PaymentDomainException {
        var command = new StripeWebhookCommand(json, stripeSignature);
        webhookHandler.handle(command);
        return ResponseEntity.ok().build();
    }
}
