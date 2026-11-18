package com.pixellabs.payment.infrastructure.security.config;

import io.grpc.ClientInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
@RequiredArgsConstructor
public class GrpcClientSecurityConfig {
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    @GlobalClientInterceptor
    ClientInterceptor keycloakInterceptor() {
        return new BearerTokenAuthenticationInterceptor(() -> {
            OAuth2AuthorizeRequest request =
                OAuth2AuthorizeRequest
                    .withClientRegistrationId("keycloak")
                    .principal("payment-api")
                    .build();
            OAuth2AuthorizedClient client = authorizedClientManager.authorize(request);
            if (client == null) {
                throw new IllegalStateException(
                    "Could not obtain Keycloak access token"
                );
            }
            return client.getAccessToken().getTokenValue();
        });
    }
}
