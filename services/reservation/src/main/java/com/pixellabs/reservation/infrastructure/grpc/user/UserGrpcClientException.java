package com.pixellabs.reservation.infrastructure.grpc.user;

public class UserGrpcClientException extends RuntimeException {
    public UserGrpcClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
