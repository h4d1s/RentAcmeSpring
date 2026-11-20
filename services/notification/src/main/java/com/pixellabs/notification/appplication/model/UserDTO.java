package com.pixellabs.notification.appplication.model;

import java.util.UUID;

public record UserDTO(
	UUID id,
	String username,
	String email,
	String roles,
	String firstName,
	String lastName
) {}
