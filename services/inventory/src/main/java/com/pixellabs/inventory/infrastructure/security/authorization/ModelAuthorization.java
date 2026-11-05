package com.pixellabs.inventory.infrastructure.security.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("modelAuthorization")
@RequiredArgsConstructor
public class ModelAuthorization {
    public boolean canCreate(Authentication authentication) {
        return hasAuthority(authentication, "model.create");
    }

    public boolean canUpdate(Authentication authentication) {
        return hasAuthority(authentication, "model.update");
    }

    public boolean canDelete(Authentication authentication) {
        return hasAuthority(authentication, "model.delete");
    }

    private boolean hasAuthority(
        Authentication authentication,
        String authority
    ) {
        return authentication.getAuthorities()
            .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), authority));
    }
}
