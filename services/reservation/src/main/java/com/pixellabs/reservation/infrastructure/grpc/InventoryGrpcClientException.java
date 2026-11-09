package com.pixellabs.reservation.infrastructure.grpc;

public class InventoryGrpcClientException extends RuntimeException {
    public InventoryGrpcClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
