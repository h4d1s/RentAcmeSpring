package com.pixellabs.reservation.application.mappers;

import com.pixellabs.reservation.infrastructure.grpc.user.dto.UserResponse;
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
