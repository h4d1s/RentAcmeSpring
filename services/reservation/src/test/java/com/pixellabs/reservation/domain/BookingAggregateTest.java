package com.pixellabs.reservation.domain;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingStatus;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingAggregateTest {
    @Test
    void createBooking_success() throws ReservationDomainException {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant pickupAt = Instant.now();
        Instant returnAt = pickupAt.plusSeconds(2 * 24 * 60 * 60);

        // Act
        Booking booking = new Booking(
            vehicleId,
            userId,
            pickupAt,
            returnAt
        );

        // Assert
        assertThat(booking).isNotNull();
    }

    @Test
    void createBooking_throwsException() {
        // Arrange
        UUID vehicleId = UUID.fromString(
            "00000000-0000-0000-0000-000000000000"
        );
        UUID userId = UUID.randomUUID();
        Instant pickupDate = Instant.now();
        Instant returnDate = null;

        // Act
        ReservationDomainException exception = assertThrows(
            ReservationDomainException.class,
            () -> new Booking(
                vehicleId,
                userId,
                pickupDate,
                returnDate
            )
        );

        // Assert
        assertThat(exception).isNotNull();
    }

    @Test
    void booking_setStatusComplete_statusChangedToComplete() throws ReservationDomainException {
        // Arrange
        Booking booking = createValidBooking();

        // Act
        booking.updateCompleteStatus();

        // Assert
        assertThat(booking.getStatus())
            .isEqualTo(BookingStatus.COMPLETED);
    }

    private Booking createValidBooking() throws ReservationDomainException {
        UUID vehicleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant pickupDate = Instant.now();
        Instant returnDate = pickupDate.plusSeconds(2 * 24 * 60 * 60);

        return new Booking(vehicleId, userId, pickupDate, returnDate);
    }
}
