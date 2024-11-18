package com.pixellabs.notification.appplication.mappers;

import org.mapstruct.factory.Mappers;

import com.pixellabs.eventbus.responses.GetUserIntegrationResponse;
import com.pixellabs.notification.appplication.model.UserDTO;

public interface UserDTOMapper {
	UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);
	
	GetUserIntegrationResponse userDTOToGetUserIntegrationResponse(UserDTO userDTO);
	UserDTO getUserIntegrationResponseToUserDTO(GetUserIntegrationResponse response);
}
