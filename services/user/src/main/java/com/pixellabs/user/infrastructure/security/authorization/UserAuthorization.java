package com.pixellabs.user.infrastructure.security.authorization;

import com.pixellabs.user.domain.userAggregate.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component("userAuthorization")
@RequiredArgsConstructor
public class UserAuthorization {
    private final UserRepository userRepository;

    public boolean canViewAny(
        Authentication authentication
    ) {
        return hasAuthority(authentication, "user.view.any");
    }

    public boolean canView(
        Authentication authentication,
        UUID id
    ) {
        if (hasAuthority(
            authentication,
            "user.view.own"
        )) {
            return true;
        }

        return hasAuthority(
            authentication,
            "user.view.own"
        ) && isOwner(authentication, id);
    }

    public boolean canUpdate(
        Authentication authentication,
        UUID id) {
        if (hasAuthority(
            authentication,
            "user.update.own"
        )) {
            return true;
        }

        return hasAuthority(
            authentication,
            "user.update.own"
        ) && isOwner(authentication, id);
    }

    public boolean canDelete(
        Authentication authentication,
        UUID id
    ) {
        if (hasAuthority(
            authentication,
            "user.delete.any"
        )) {
            return true;
        }

        return hasAuthority(
            authentication,
            "user.delete.own"
        ) && isOwner(authentication, id);
    }

    private boolean hasAuthority(
        Authentication authentication,
        String authority
    ) {
        return authentication.getAuthorities()
            .stream()
            .anyMatch(a -> Objects.equals(a.getAuthority(), authority));
    }

    private boolean isOwner(
        Authentication authentication,
        UUID id
    ) {
        return userRepository.existsByIdAndExternalId(
            id,
            authentication.getName()
        );
    }
}