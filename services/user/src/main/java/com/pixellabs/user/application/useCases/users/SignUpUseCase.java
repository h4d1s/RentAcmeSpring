package com.pixellabs.user.application.useCases.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.user.application.useCases.users.dtos.SignUpRequest;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class SignUpUseCase {

	@Autowired
	private final UserRepository userRepository;
	
	//@Autowired
    //private PasswordEncoder passwordEncoder;
	
	public SignUpUseCase(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void execute(SignUpRequest request) {
		System.out.print(request.getPassword() + " " + request.getConfirmPassword());
		if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
		
		if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("User already exists.");
        }
		
		User user = new User();
		user.setUsername(request.getUsername());
		//user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles("USER");
		
		userRepository.save(user);
	}
}
