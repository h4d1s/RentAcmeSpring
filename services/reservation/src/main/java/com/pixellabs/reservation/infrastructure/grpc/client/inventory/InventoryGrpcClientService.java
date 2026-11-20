package com.pixellabs.reservation.infrastructure.grpc.client.inventory;

import com.pixellabs.inventory.grpc.CheckVehicleRequest;
import com.pixellabs.inventory.grpc.GetVehicleRequest;
import com.pixellabs.inventory.grpc.GetVehicleResponse;
import com.pixellabs.inventory.grpc.InventoryProtoServiceGrpc;
import com.pixellabs.inventory.grpc.VehicleStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryGrpcClientService {
    private final InventoryProtoServiceGrpc.InventoryProtoServiceBlockingStub inventoryStub;

    public GetVehicleResponse getVehicle(UUID vehicleId) {
        GetVehicleRequest request = GetVehicleRequest.newBuilder()
            .setVehicleId(vehicleId.toString())
            .build();

        return inventoryStub.getVehicle(request);
    }

    public boolean checkVehicle(UUID vehicleId) {
        CheckVehicleRequest request = CheckVehicleRequest.newBuilder()
            .setVehicleId(vehicleId.toString())
            .build();
        VehicleStatusResponse response = inventoryStub.checkVehicle(request);

        return response.getIsExists();
    }
}
