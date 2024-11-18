package com.pixellabs.notification.infrastructure.services.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.requests.GetUserIntegrationRequest;
import com.pixellabs.eventbus.responses.GetUserIntegrationResponse;
import com.pixellabs.notification.appplication.mappers.UserDTOMapper;
import com.pixellabs.notification.appplication.model.UserDTO;
import com.pixellabs.notification.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	public IntegrationEventService integrationEventService;
	
	public UserServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserDTO get(UUID id) {
		GetUserIntegrationRequest request = new GetUserIntegrationRequest(id);
		GetUserIntegrationResponse response = (GetUserIntegrationResponse) integrationEventService.sendAndReceive(request, BusConstants.EXCHANGE_USER, BusConstants.QUEUE_USER_GET_REQUEST);
		UserDTO userDTO = UserDTOMapper.INSTANCE.getUserIntegrationResponseToUserDTO(response);
		
		return userDTO;
	}

}
