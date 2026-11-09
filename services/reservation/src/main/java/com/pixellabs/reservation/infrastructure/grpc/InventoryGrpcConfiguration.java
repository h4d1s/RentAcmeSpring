package com.pixellabs.reservation.infrastructure.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryGrpcConfiguration {
    @Bean(destroyMethod = "shutdown")
    public ManagedChannel inventoryGrpcChannel(
        @Value("${inventory.grpc.host}") String host,
        @Value("${inventory.grpc.port}") int port) {

        return ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .build();
    }

    @Bean
    public InventoryGrpcClient inventoryGrpcClient(
        ManagedChannel inventoryGrpcChannel) {
        return new InventoryGrpcClient(inventoryGrpcChannel);
    }
}
