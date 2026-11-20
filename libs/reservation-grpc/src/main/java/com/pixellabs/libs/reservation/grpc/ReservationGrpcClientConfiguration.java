package com.pixellabs.libs.reservation.grpc;

import com.pixellabs.libs.security.grpc.AccessTokenProvider;
import com.pixellabs.libs.security.grpc.OAuth2CallCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
@RequiredArgsConstructor
public class ReservationGrpcClientConfiguration {

    @Bean
    public BookingProtoServiceGrpc.BookingProtoServiceBlockingStub bookingBlockingStub(
        GrpcChannelFactory channels,
        AccessTokenProvider tokenProvider) {
        return BookingProtoServiceGrpc
            .newBlockingStub(channels.createChannel("reservation-service"))
            .withCallCredentials(
                new OAuth2CallCredentials(
                    tokenProvider,
                    "keycloak",
                    "reservation-service"
                )
            );
    }
}