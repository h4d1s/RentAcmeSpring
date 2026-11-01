package com.pixellabs.notification.appplication.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private UUID id;
	private String username;
	private String email;
	private String roles;
	private String firstName;
	private String lastName;
}
