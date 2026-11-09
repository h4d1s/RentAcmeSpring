package com.pixellabs.reservation.infrastructure.security.authorization;

import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component("bookingAuthorization")
@RequiredArgsConstructor
public class BookingAuthorization {
    private final BookingRepository bookingRepository;

    public boolean canReserve(Authentication authentication) {
        return hasAuthority(authentication, "booking.reserve");
    }

    public boolean canCancel(Authentication authentication) {
        return hasAuthority(authentication, "booking.cancel");
    }

    public boolean canComplete(Authentication authentication) {
        return hasAuthority(authentication, "booking.complete");
    }

    public boolean canViewAll(
        Authentication authentication
    ) {
        return hasAuthority(authentication, "booking.view.any");
    }

    public boolean canView(
        Authentication authentication,
        UUID id
    ) {
        if (hasAuthority(
            authentication,
            "booking.view.any"
        )) {
            return true;
        }

        return hasAuthority(
            authentication,
            "booking.view.own"
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
        return bookingRepository.existsByIdAndUserId(
            id,
            UUID.fromString(authentication.getName())
        );
    }
}