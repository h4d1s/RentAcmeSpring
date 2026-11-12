package com.pixellabs.user.infrastructure.grpc;

import com.pixellabs.user.application.exceptions.NotFoundException;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import com.pixellabs.user.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserGrpcServerService
    extends UserProtoServiceGrpc.UserProtoServiceImplBase {
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("@userAuthorization.canViewAny(authentication)")
    public void userExists(
        UserExistsRequest request,
        StreamObserver<UserExistsResponse> responseObserver) {

        log.info("Checking user {}", request.getUserId());
        try {
            UUID userId = UUID.fromString(request.getUserId());
            boolean exists = userRepository.existsById(userId);

            UserExistsResponse response = UserExistsResponse.newBuilder()
                .setExists(exists)
                .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException ex) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid user ID.")
                    .asRuntimeException()
            );
        } catch (Exception ex) {
            log.error("Error checking user.", ex);

            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("An error occurred while checking the user.")
                    .asRuntimeException()
            );
        }
    }

    @Override
    @PreAuthorize("@userAuthorization.canViewAny(authentication)")
    public void getUser(
        GetUserRequest request,
        StreamObserver<GetUserResponse> responseObserver) {
        log.info("Getting user {}", request.getUserId());

        try {
            UUID userId = UUID.fromString(request.getUserId());
            User user = userRepository.findById(userId).orElse(null);

            GetUserResponse response = Optional.ofNullable(user)
                .map(u -> GetUserResponse.newBuilder()
                    .setId(u.getId().toString())
                    .setExternalId(u.getExternalId())
                    .setEmail(u.getEmail())
                    .setFirstName(u.getFirstName())
                    .setLastName(u.getLastName())
                    .build())
                .orElseGet(() -> GetUserResponse.newBuilder().build());

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException ex) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid user ID.")
                    .asRuntimeException()
            );
        } catch (Exception ex) {
            log.error("Error getting user", ex);

            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("An error occurred while retrieving the user.")
                    .asRuntimeException()
            );
        }
    }

    @Override
    @PreAuthorize("@userAuthorization.canViewAny(authentication)")
    public void getUserByExternalId(
        GetUserByExternalIdRequest request,
        StreamObserver<GetUserByExternalIdResponse> responseObserver) {
        log.info("Getting user by external ID: {}", request.getUserExternalId());

        try {
            User user = userRepository.findByExternalId(request.getUserExternalId())
                .orElseThrow(() -> new NotFoundException("User not found."));

            GetUserByExternalIdResponse response =
                GetUserByExternalIdResponse.newBuilder()
                    .setId(user.getId().toString())
                    .setExternalId(user.getExternalId())
                    .setEmail(user.getEmail())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            log.error("Error getting user by external ID", ex);

            responseObserver.onError(
                Status.INTERNAL
                    .withDescription("An error occurred while retrieving the user.")
                    .asRuntimeException()
            );
        }
    }
}
