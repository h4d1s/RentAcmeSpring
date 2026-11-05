package com.pixellabs.inventory.infrastructure.security.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("vehicleAuthorization")
@RequiredArgsConstructor
public class VehicleAuthorization {
    public boolean canCreate(Authentication authentication) {
        return hasAuthority(authentication, "vehicle.create");
    }

    public boolean canUpdate(Authentication authentication) {
        return hasAuthority(authentication, "vehicle.update");
    }

    public boolean canDelete(Authentication authentication) {
        return hasAuthority(authentication, "vehicle.delete");
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
