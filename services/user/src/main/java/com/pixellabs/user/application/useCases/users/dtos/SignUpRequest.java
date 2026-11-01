package com.pixellabs.user.application.useCases.users.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class SignUpRequest {
	@NotBlank(message = "Email is required")
	private String email;
	@NotBlank(message = "Username is required")
    private String username;
	@NotBlank(message = "Password is required")
    private String password;
	@NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
