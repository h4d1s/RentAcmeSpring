package com.pixellabs.payment.application.useCases.stripeWebhook;

import jakarta.validation.constraints.NotBlank;

public record StripeWebhookCommand(
    @NotBlank(message = "Webhook JSON cannot be empty")
    String json,

    @NotBlank(message = "Stripe signature cannot be empty")
    String stripeSignature
) {}
