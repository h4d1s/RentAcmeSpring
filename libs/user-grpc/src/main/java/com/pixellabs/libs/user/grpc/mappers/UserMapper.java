package com.pixellabs.libs.user.grpc.mappers;

import com.pixellabs.libs.user.grpc.models.UserResponse;
import com.pixellabs.user.grpc.GetUserByExternalIdResponse;
import com.pixellabs.user.grpc.GetUserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(GetUserResponse response);
    UserResponse toUserResponse(
        GetUserByExternalIdResponse response
    );
}
