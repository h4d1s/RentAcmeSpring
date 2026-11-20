package com.pixellabs.libs.user.grpc;

import com.pixellabs.libs.security.grpc.AccessTokenProvider;
import com.pixellabs.libs.security.grpc.OAuth2CallCredentials;
import com.pixellabs.user.grpc.UserProtoServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@RequiredArgsConstructor
@Configuration
public class UserGrpcClientConfiguration {
    @Bean
    UserProtoServiceGrpc.UserProtoServiceBlockingStub userBlockingStub(
        GrpcChannelFactory channels,
        AccessTokenProvider tokenProvider) {
        return UserProtoServiceGrpc
            .newBlockingStub(channels.createChannel("user-service"))
            .withCallCredentials(
                new OAuth2CallCredentials(
                    tokenProvider,
                    "keycloak",
                    "user-service"
                )
            );
    }
}
