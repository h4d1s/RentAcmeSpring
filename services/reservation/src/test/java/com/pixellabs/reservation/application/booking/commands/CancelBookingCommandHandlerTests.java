package com.pixellabs.reservation.application.booking.commands;

import com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking.CancelBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking.CancelBookingHandler;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.bookingAggregate.BookingStatus;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelBookingCommandHandlerTests {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CancelBookingHandler handler;

    @Test
    void handle_WhenBookingExists_ShouldCancelBooking() throws ReservationDomainException {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        Booking booking = createBooking();

        when(bookingRepository.findById(bookingId))
            .thenReturn(Optional.of(booking));

        CancelBookingCommand command = new CancelBookingCommand(bookingId);

        // Act
        handler.handle(command);

        // Assert
        assertThat(booking.getStatus())
            .isEqualTo(BookingStatus.CANCELED);
    }

    @Test
    void handle_WhenBookingDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        UUID bookingId = UUID.randomUUID();

        when(bookingRepository.findById(bookingId))
            .thenReturn(Optional.empty());

        CancelBookingCommand command = new CancelBookingCommand(bookingId);

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void handle_WhenBookingIsCompleted_ShouldThrowReservationDomainException() throws ReservationDomainException {
        // Arrange
        UUID bookingId = UUID.randomUUID();

        Booking booking = createBooking();
        booking.updateCompleteStatus();

        when(bookingRepository.findById(bookingId))
            .thenReturn(Optional.of(booking));

        CancelBookingCommand command = new CancelBookingCommand(bookingId);

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ReservationDomainException.class);

        assertThat(booking.getStatus())
            .isEqualTo(BookingStatus.COMPLETED);
    }

    private Booking createBooking() throws ReservationDomainException {
        return new Booking(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(3)
        );
    }
}

