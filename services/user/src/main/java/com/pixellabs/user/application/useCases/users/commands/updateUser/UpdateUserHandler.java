package com.pixellabs.user.application.useCases.users.commands.updateUser;

import com.pixellabs.user.application.exceptions.NotFoundException;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import com.pixellabs.user.domain.userAggregate.exceptions.UserDomainException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserHandler {
    private final UserRepository userRepository;

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User handle(UUID id, UpdateUserCommand command) throws UserDomainException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found."));

        user.updateFirstName(command.firstName());
        user.updateLastName(command.lastName());
        user.updateEmail(command.email());
        user.updateUsername(command.userName());
        user.updatePhoneNumber(command.phoneNumber());
        user.updateAddress(command.street(), command.city(), command.postalCode(), command.country());

        return userRepository.save(user);
    }
}