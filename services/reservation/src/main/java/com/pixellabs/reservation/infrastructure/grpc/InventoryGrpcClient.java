package com.pixellabs.reservation.infrastructure.grpc;

import com.pixellabs.inventory.grpc.CheckVehicleRequest;
import com.pixellabs.inventory.grpc.GetVehicleRequest;
import com.pixellabs.inventory.grpc.GetVehicleResponse;
import com.pixellabs.inventory.grpc.InventoryProtoServiceGrpc;
import com.pixellabs.inventory.grpc.VehicleStatusResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class InventoryGrpcClient {
    private final InventoryProtoServiceGrpc.InventoryProtoServiceBlockingStub blockingStub;

    public InventoryGrpcClient(ManagedChannel channel) {
        this.blockingStub = InventoryProtoServiceGrpc.newBlockingStub(channel);
    }

    public GetVehicleResponse getVehicle(String vehicleId) {
        GetVehicleRequest request = GetVehicleRequest.newBuilder()
            .setVehicleId(vehicleId)
            .build();

        try {
            return blockingStub.getVehicle(request);
        } catch (StatusRuntimeException exception) {
            throw new InventoryGrpcClientException(
                "Failed to retrieve vehicle: " + vehicleId,
                exception
            );
        }
    }

    public boolean checkVehicle(String vehicleId) {
        CheckVehicleRequest request = CheckVehicleRequest.newBuilder()
            .setVehicleId(vehicleId)
            .build();

        try {
            VehicleStatusResponse response = blockingStub.checkVehicle(request);

            return response.getIsExists();
        } catch (StatusRuntimeException exception) {
            throw new InventoryGrpcClientException(
                "Failed to check vehicle: " + vehicleId, exception
            );
        }
    }
}
