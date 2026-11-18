package com.pixellabs.payment.infrastructure.payment.configs;

import com.stripe.StripeClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StripeProperties.class)
public class StripeConfiguration {
    @Bean
    public StripeClient stripeClient(StripeProperties properties) {
        return new StripeClient(properties.api().key());
    }
}
