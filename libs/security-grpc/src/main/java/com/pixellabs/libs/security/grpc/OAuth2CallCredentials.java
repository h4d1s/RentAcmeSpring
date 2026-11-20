package com.pixellabs.libs.security.grpc;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;

import java.util.concurrent.Executor;

public class OAuth2CallCredentials extends CallCredentials {

    private final AccessTokenProvider tokenProvider;
    private final String registrationId;
    private final String principalName;

    public OAuth2CallCredentials(
        AccessTokenProvider tokenProvider,
        String registrationId,
        String principalName) {
        this.tokenProvider = tokenProvider;
        this.registrationId = registrationId;
        this.principalName = principalName;
    }

    @Override
    public void applyRequestMetadata(
        RequestInfo requestInfo,
        Executor appExecutor,
        MetadataApplier applier) {

        appExecutor.execute(() -> {
            try {
                String token = tokenProvider.getAccessToken(
                    registrationId,
                    principalName
                );

                Metadata headers = new Metadata();

                Metadata.Key<String> authorizationKey =
                    Metadata.Key.of(
                        "authorization",
                        Metadata.ASCII_STRING_MARSHALLER
                    );

                headers.put(authorizationKey, "Bearer " + token);
                applier.apply(headers);

            } catch (Exception exception) {
                applier.fail(
                    Status.UNAUTHENTICATED
                        .withDescription("Failed to obtain access token")
                        .withCause(exception)
                );
            }
        });
    }
}
