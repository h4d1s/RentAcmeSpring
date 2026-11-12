package com.pixellabs.reservation.infrastructure.grpc.inventory;

import com.pixellabs.inventory.grpc.InventoryProtoServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class InventoryGrpcConfiguration {
    @Bean
    public InventoryProtoServiceGrpc.InventoryProtoServiceBlockingStub inventoryBlockingStub(
        GrpcChannelFactory channels) {
        return InventoryProtoServiceGrpc.newBlockingStub(
            channels.createChannel("inventory-service")
        );
    }
}
