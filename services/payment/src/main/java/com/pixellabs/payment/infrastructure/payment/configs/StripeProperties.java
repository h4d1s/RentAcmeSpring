package com.pixellabs.payment.infrastructure.payment.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeProperties(
    Api api,
    Webhook webhook
) {
    public record Api(
        String key,
        String publishableKey
    ) {}
    public record Webhook(
        String secret
    ) {}
}
