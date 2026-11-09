package com.pixellabs.reservation.api;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@UtilityClass
public final class TestAuthentication {
    public static Authentication userWithAllBookingPermissions() {
        return new UsernamePasswordAuthenticationToken(
            "test-user",
            "password",
            List.of(
                new SimpleGrantedAuthority("booking.reserve"),
                new SimpleGrantedAuthority("booking.cancel"),
                new SimpleGrantedAuthority("booking.complete"),
                new SimpleGrantedAuthority("booking.view.own"),
                new SimpleGrantedAuthority("booking.view.any")
            )
        );
    }

    public static Authentication userWithoutPermissions() {
        return new UsernamePasswordAuthenticationToken(
            "test-user",
            "password",
            List.of()
        );
    }
}
