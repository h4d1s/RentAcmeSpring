package com.pixellabs.reservation.infrastructure.grpc.user;

import com.pixellabs.user.grpc.UserProtoServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class UserGrpcConfiguration {
    @Bean
    public UserProtoServiceGrpc.UserProtoServiceBlockingStub userBlockingStub(
        GrpcChannelFactory channels) {
        return UserProtoServiceGrpc.newBlockingStub(
            channels.createChannel("user-service")
        );
    }
}
