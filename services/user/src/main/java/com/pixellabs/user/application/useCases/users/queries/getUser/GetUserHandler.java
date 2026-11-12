package com.pixellabs.user.application.useCases.users.queries.getUser;

import com.pixellabs.user.application.exceptions.NotFoundException;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserHandler {
    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#query.id()")
    @Transactional
    public User handle(GetUserQuery query)
        throws NotFoundException {
        return userRepository.findById(query.id())
            .orElseThrow(() -> new NotFoundException("User not found."));
    }
}