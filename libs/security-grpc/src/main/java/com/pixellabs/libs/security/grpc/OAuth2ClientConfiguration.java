package com.pixellabs.libs.security.grpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class OAuth2ClientConfiguration {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
        ClientRegistrationRepository registrations,
        OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider provider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
            new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                registrations,
                authorizedClientService
            );
        manager.setAuthorizedClientProvider(provider);

        return manager;
    }

    @Bean
    RestClient restClient(
        OAuth2AuthorizedClientManager manager) {
        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(manager);
        interceptor.setClientRegistrationIdResolver(request -> "keycloak");
        return RestClient.builder()
            .requestInterceptor(interceptor)
            .build();
    }
}
