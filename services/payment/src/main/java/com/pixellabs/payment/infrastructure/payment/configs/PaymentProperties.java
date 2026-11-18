package com.pixellabs.payment.infrastructure.payment.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.payment")
public record PaymentProperties(String currency) {}
