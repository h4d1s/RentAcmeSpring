package com.pixellabs.inventory.domain.bookingAggregate;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
