package com.pixellabs.user.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixellabs.user.application.useCases.users.SignUpUseCase;
import com.pixellabs.user.application.useCases.users.dtos.SignUpRequest;

@RestController
@RequestMapping("/api")
public class UserController {
	
	public UserController() {
	}
}
