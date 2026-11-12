package com.pixellabs.reservation.infrastructure.grpc.user;

import com.pixellabs.reservation.application.mappers.UserMapper;
import com.pixellabs.reservation.infrastructure.grpc.user.dto.UserResponse;
import com.pixellabs.user.grpc.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserGrpcClient {
    private final UserProtoServiceGrpc.UserProtoServiceBlockingStub userStub;
    private final UserMapper userMapper;

    public Optional<UserResponse> getUser(UUID userId) {
        GetUserRequest request = GetUserRequest.newBuilder()
            .setUserId(userId.toString())
            .build();

        try {
            GetUserResponse response = userStub.getUser(request);
            return Optional.of(userMapper.toUserResponse(response));
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return Optional.empty();
            }
            throw new UserGrpcClientException(
                "Failed to retrieve user: " + userId, ex
            );
        }
    }

    public Optional<UserResponse> getUserByExternalId(String externalId) {
        GetUserByExternalIdRequest request = GetUserByExternalIdRequest.newBuilder()
                .setUserExternalId(externalId)
                .build();

        try {
            GetUserByExternalIdResponse response = userStub.getUserByExternalId(request);
            return Optional.of(userMapper.toUserResponse(response));
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return Optional.empty();
            }
            throw new UserGrpcClientException(
                "Failed to retrieve user by external ID: " + externalId, ex
            );
        }
    }

    public boolean existsUser(UUID userId) {
        UserExistsRequest request = UserExistsRequest.newBuilder()
            .setUserId(userId.toString())
            .build();

        try {
            UserExistsResponse response = userStub.userExists(request);
            return response.getExists();
        } catch (StatusRuntimeException ex) {
            throw new UserGrpcClientException(
                "Failed to check user existence: " + userId, ex
            );
        }
    }
}
