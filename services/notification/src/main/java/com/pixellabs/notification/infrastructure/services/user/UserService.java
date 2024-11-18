package com.pixellabs.notification.infrastructure.services.user;

import java.util.UUID;

import com.pixellabs.notification.appplication.model.UserDTO;

public interface UserService {
	UserDTO get(UUID id);
}
