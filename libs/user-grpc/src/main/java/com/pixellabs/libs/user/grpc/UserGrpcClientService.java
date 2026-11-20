package com.pixellabs.libs.user.grpc;

import com.pixellabs.libs.user.grpc.models.UserResponse;
import com.pixellabs.libs.user.grpc.mappers.UserMapper;
import com.pixellabs.user.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserGrpcClientService {
    private final UserProtoServiceGrpc.UserProtoServiceBlockingStub userStub;
    private final UserMapper userMapper;

    public UserResponse getUser(UUID userId) {
        GetUserRequest request = GetUserRequest.newBuilder()
            .setUserId(userId.toString())
            .build();
        GetUserResponse response = userStub.getUser(request);
        return userMapper.toUserResponse(response);
    }

    public Optional<UserResponse> getUserByExternalId(String externalId) {
        GetUserByExternalIdRequest request = GetUserByExternalIdRequest.newBuilder()
                .setUserExternalId(externalId)
                .build();
        GetUserByExternalIdResponse response = userStub.getUserByExternalId(request);
        return Optional.of(userMapper.toUserResponse(response));
    }

    public boolean existsUser(UUID userId) {
        UserExistsRequest request = UserExistsRequest.newBuilder()
            .setUserId(userId.toString())
            .build();

        UserExistsResponse response = userStub.userExists(request);
        return response.getExists();
    }
}
