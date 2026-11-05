package com.pixellabs.inventory.infrastructure.grpc;

import com.pixellabs.inventory.domain.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.libs.contracts.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryGrpcServerService
        extends InventoryProtoServiceGrpc.InventoryProtoServiceImplBase {

    private final VehicleRepository vehicleRepository;

    @Override
    public void getVehicle(
        GetVehicleRequest request,
        StreamObserver<GetVehicleResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getVehicleId());
            Vehicle vehicle = vehicleRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Vehicle not found."));

            var response = GetVehicleResponse.newBuilder()
                    .setRegistrationPlates(vehicle.getRegistrationPlates())
                    .setRentalPricePerDay(vehicle.getRentalPricePerDay().doubleValue())
                    .setVariantId(vehicle.getVariant().getId().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            log.error("Failed to get vehicle", ex);
            responseObserver.onError(
                Status.INTERNAL
                        .withDescription("Failed to get vehicle")
                        .withCause(ex)
                        .asRuntimeException()
            );
        }
    }

    @Override
    public void checkVehicle(
            CheckVehicleRequest request,
            StreamObserver<VehicleStatusResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getVehicleId());
            boolean exists = vehicleRepository.existsById(id);
            responseObserver.onNext(
                VehicleStatusResponse.newBuilder()
                        .setIsExists(exists)
                        .build()
            );
            responseObserver.onCompleted();
        } catch (Exception ex) {
            log.error("Failed to check vehicle", ex);
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("Failed to check vehicle")
                    .withCause(ex)
                    .asRuntimeException()
            );
        }
    }
}
