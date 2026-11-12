package com.pixellabs.reservation.application.booking.queries;

import com.pixellabs.reservation.application.useCases.bookings.queries.getBooking.GetBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBooking.GetBookingQuery;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
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
public class GetBookingQueryHandlerTests {
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private GetBookingHandler handler;

    @Test
    void handle_WhenBookingExists_ShouldReturnBooking()
        throws ReservationDomainException {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        Booking booking = createBooking();

        when(bookingRepository.findById(bookingId))
            .thenReturn(Optional.of(booking));

        GetBookingQuery query = new GetBookingQuery(bookingId);

        // Act
        Booking result = handler.handle(query);

        // Assert
        assertThat(result).isSameAs(booking);
    }

    @Test
    void handle_WhenBookingDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        UUID bookingId = UUID.randomUUID();

        when(bookingRepository.findById(bookingId))
            .thenReturn(Optional.empty());

        GetBookingQuery query = new GetBookingQuery(bookingId);

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(query))
            .isInstanceOf(NotFoundException.class);
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
