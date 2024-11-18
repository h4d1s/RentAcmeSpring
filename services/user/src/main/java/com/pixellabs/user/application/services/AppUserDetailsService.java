package com.pixellabs.user.application.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	public AppUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
		
		return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
	}

}
