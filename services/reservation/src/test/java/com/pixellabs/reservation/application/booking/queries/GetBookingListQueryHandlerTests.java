package com.pixellabs.reservation.application.booking.queries;

import com.pixellabs.reservation.infrastructure.caching.services.BookingCacheService;
import com.pixellabs.reservation.application.useCases.bookings.dtos.BookingPageCache;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList.GetBookingListHandler;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList.GetBookingListQuery;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pixellabs.reservation.application.exceptions.NotFoundException;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetBookingListQueryHandlerTests {
    @Mock
    private BookingCacheService bookingCacheService;

    @InjectMocks
    private GetBookingListHandler handler;

    @Test
    void handle_WhenBookingsExist_ShouldReturnBookingPage() {
        // Arrange
        GetBookingListQuery query = mock(GetBookingListQuery.class);

        Booking booking1 = mock(Booking.class);
        Booking booking2 = mock(Booking.class);

        List<Booking> bookings = List.of(booking1, booking2);
        BookingPageCache cache = new BookingPageCache(
            bookings,
            0,
            10,
            2L,
            1
        );

        when(bookingCacheService.getBookings(query))
            .thenReturn(cache);

        // Act
        Page<Booking> result = handler.handle(query);

        // Assert
        assertThat(result.getContent()).containsExactly(booking1, booking2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);

        verify(bookingCacheService).getBookings(query);
    }

    @Test
    void handle_WhenNoBookingsExist_ShouldReturnEmptyPage() {
        // Arrange
        GetBookingListQuery query = mock(GetBookingListQuery.class);

        BookingPageCache cache = new BookingPageCache(
            List.of(),
            0,
            10,
            0L,
            0
        );

        when(bookingCacheService.getBookings(query))
            .thenReturn(cache);

        // Act
        Page<Booking> result = handler.handle(query);

        // Assert
        assertThat(result).isEmpty();
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isZero();

        verify(bookingCacheService).getBookings(query);
    }

    @Test
    void handle_WhenCacheServiceThrowsNotFoundException_ShouldPropagateException() {
        // Arrange
        GetBookingListQuery query = mock(GetBookingListQuery.class);

        when(bookingCacheService.getBookings(query))
            .thenThrow(new NotFoundException("Bookings not found."));

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(query))
            .isInstanceOf(NotFoundException.class);

        verify(bookingCacheService).getBookings(query);
    }
}
