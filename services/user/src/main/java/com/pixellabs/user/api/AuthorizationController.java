package com.pixellabs.user.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pixellabs.user.application.useCases.users.SignUpUseCase;
import com.pixellabs.user.application.useCases.users.dtos.SignUpRequest;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthorizationController {

	private final SignUpUseCase signUpUseCase;
	
	public AuthorizationController(SignUpUseCase signUpUseCase) {
		this.signUpUseCase = signUpUseCase;
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		return "login";
	}
	
	@GetMapping("/signup")
	public String showSignupForm(Model model) {
		model.addAttribute("signUpRequest", new SignUpRequest());
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signUp(@Valid @ModelAttribute("signUpRequest") SignUpRequest request, BindingResult result) {
		if (result.hasErrors()) {
            return "signup";
        }
		
		try {
			signUpUseCase.execute(request);
		} catch (Exception e) {
			 result.reject("signup.error", e.getMessage());
			 return "signup";
		}
		return "redirect:/auth/login";
	}
}
