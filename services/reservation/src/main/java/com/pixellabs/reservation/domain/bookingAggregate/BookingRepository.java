package com.pixellabs.reservation.domain.bookingAggregate;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingRepository
    extends JpaRepository<Booking, UUID>,
        JpaSpecificationExecutor<Booking> {
    boolean existsByIdAndUserId(UUID id, UUID userId);
}
