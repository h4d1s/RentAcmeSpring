package com.pixellabs.reservation.domain.bookingAggregate;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository
    extends JpaRepository<Booking, UUID>,
        JpaSpecificationExecutor<Booking> {
    boolean existsByIdAndUserId(UUID id, UUID userId);

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM Booking b
        WHERE b.vehicleId = :vehicleId
          AND b.status = :status
          AND b.pickupAt < :returnAt
          AND b.returnAt > :pickupAt
        """)
    boolean isVehicleReserved(
        UUID vehicleId,
        Instant pickupAt,
        Instant returnAt,
        BookingStatus status
    );
}
