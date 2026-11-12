package com.pixellabs.reservation.infrastructure.grpc.inventory;

import com.pixellabs.inventory.grpc.CheckVehicleRequest;
import com.pixellabs.inventory.grpc.GetVehicleRequest;
import com.pixellabs.inventory.grpc.GetVehicleResponse;
import com.pixellabs.inventory.grpc.InventoryProtoServiceGrpc;
import com.pixellabs.inventory.grpc.VehicleStatusResponse;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryGrpcClient {
    private final InventoryProtoServiceGrpc.InventoryProtoServiceBlockingStub inventoryStub;

    public GetVehicleResponse getVehicle(UUID vehicleId) {
        GetVehicleRequest request = GetVehicleRequest.newBuilder()
            .setVehicleId(vehicleId.toString())
            .build();

        try {
            return inventoryStub.getVehicle(request);
        } catch (StatusRuntimeException exception) {
            throw new InventoryGrpcClientException(
                "Failed to retrieve vehicle: " + vehicleId,
                exception
            );
        }
    }

    public boolean checkVehicle(UUID vehicleId) {
        CheckVehicleRequest request = CheckVehicleRequest.newBuilder()
            .setVehicleId(vehicleId.toString())
            .build();

        try {
            VehicleStatusResponse response = inventoryStub.checkVehicle(request);

            return response.getIsExists();
        } catch (StatusRuntimeException exception) {
            throw new InventoryGrpcClientException(
                "Failed to check vehicle: " + vehicleId, exception
            );
        }
    }
}
