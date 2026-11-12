package com.pixellabs.user.application.useCases.users.commands.deleteUser;

import com.pixellabs.user.application.exceptions.NotFoundException;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserHandler {
    private final UserRepository userRepository;

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void handle(DeleteUserCommand command)
        throws NotFoundException {
        User user = userRepository.findById(command.id())
            .orElseThrow(() -> new NotFoundException("User not found."));

        userRepository.delete(user);
    }
}
