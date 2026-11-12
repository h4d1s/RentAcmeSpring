package com.pixellabs.reservation.infrastructure.grpc.inventory;

public class InventoryGrpcClientException extends RuntimeException {
    public InventoryGrpcClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
