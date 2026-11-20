package com.pixellabs.libs.security.grpc;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenProvider {

    private final OAuth2AuthorizedClientManager clientManager;

    public String getAccessToken(String registrationId, String principalName) {

        OAuth2AuthorizeRequest request =
            OAuth2AuthorizeRequest
                .withClientRegistrationId(registrationId)
                .principal(principalName)
                .build();

        OAuth2AuthorizedClient client = clientManager.authorize(request);

        if (client == null) {
            throw new IllegalStateException(
                "Failed to obtain OAuth2 access token for: " + registrationId
            );
        }

        return client.getAccessToken().getTokenValue();
    }
}
