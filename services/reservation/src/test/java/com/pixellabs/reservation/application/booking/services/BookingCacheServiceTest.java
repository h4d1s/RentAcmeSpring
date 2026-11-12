package com.pixellabs.reservation.application.booking.services;

import com.pixellabs.reservation.infrastructure.caching.services.BookingCacheService;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pixellabs.reservation.application.useCases.bookings.dtos.BookingPageCache;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList.GetBookingListQuery;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingCacheServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingCacheService bookingCacheService;

    private List<Booking> bookings;

    @BeforeEach
    void setUp() {
        bookings = List.of(
            mock(Booking.class),
            mock(Booking.class)
        );
    }

    @Test
    void getBookings_WhenBookingsExist_ShouldReturnBookingPageCache() {
        // Arrange
        GetBookingListQuery query = createQuery(
            0,
            10,
            null,
            "asc"
        );

        Pageable pageable = PageRequest.of(
            0,
            10,
            Sort.by(Sort.Direction.ASC, "id")
        );

        Page<Booking> repositoryPage =
            new PageImpl<>(bookings, pageable, 2);

        when(bookingRepository.findAll(
            Mockito.<Specification<Booking>>any(),
            any(Pageable.class)
        )).thenReturn(repositoryPage);

        // Act
        BookingPageCache result = bookingCacheService.getBookings(query);

        // Assert
        assertThat(result.content()).hasSize(2);
        assertThat(result.content()).isEqualTo(bookings);
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(1);

        verify(bookingRepository).findAll(
            Mockito.<Specification<Booking>>any(),
            eq(pageable)
        );
    }

    @Test
    void getBookings_WhenNoBookingsExist_ShouldReturnEmptyPageCache() {
        // Arrange
        GetBookingListQuery query = createQuery(
            0,
            10,
            null,
            "asc"
        );

        Pageable pageable = PageRequest.of(
            0,
            10,
            Sort.by(Sort.Direction.ASC, "id")
        );

        Page<Booking> emptyPage =
            new PageImpl<>(List.of(), pageable, 0);

        when(bookingRepository.findAll(
            Mockito.<Specification<Booking>>any(),
            any(Pageable.class)
        )).thenReturn(emptyPage);

        // Act
        BookingPageCache result = bookingCacheService.getBookings(query);

        // Assert
        assertThat(result.content()).isEmpty();
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.totalElements()).isZero();
        assertThat(result.totalPages()).isZero();
    }

    @Test
    void getBookings_WhenPageIsGreaterThanOne_ShouldUseZeroBasedPage() {
        // Arrange
        GetBookingListQuery query = createQuery(
            3,
            5,
            null,
            "asc"
        );

        when(bookingRepository.findAll(
            Mockito.<Specification<Booking>>any(),
            any(Pageable.class)
        )).thenReturn(Page.empty());

        // Act
        bookingCacheService.getBookings(query);

        // Assert
        ArgumentCaptor<Pageable> captor =
            ArgumentCaptor.forClass(Pageable.class);

        verify(bookingRepository).findAll(
            Mockito.<Specification<Booking>>any(),
            captor.capture()
        );

        assertThat(captor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    void getBookings_WhenSortingByPickupDate_ShouldSortByPickupDate() {
        assertSorting("pickupDate", "pickupDate", "asc");
    }

    @Test
    void getBookings_WhenSortingByReturnDate_ShouldSortByReturnDate() {
        assertSorting("returnDate", "returnDate", "asc");
    }

    @Test
    void getBookings_WhenSortingByStatus_ShouldSortByStatus() {
        assertSorting("status", "status", "asc");
    }

    @Test
    void getBookings_WhenSortingByUnknownField_ShouldSortById() {
        assertSorting("unknown", "id", "asc");
    }

    @Test
    void getBookings_WhenOrderIsDescending_ShouldUseDescendingSort() {
        assertSorting("pickupDate", "pickupDate", "desc");
    }

    @Test
    void getBookings_WhenPageAndPageSizeAreZero_ShouldUseMinimumValues() {
        // Arrange
        GetBookingListQuery query = createQuery(
            0,
            0,
            null,
            null
        );

        when(bookingRepository.findAll(
            Mockito.<Specification<Booking>>any(),
            any(Pageable.class)
        )).thenReturn(Page.empty());

        // Act
        bookingCacheService.getBookings(query);

        // Assert
        ArgumentCaptor<Pageable> captor =
            ArgumentCaptor.forClass(Pageable.class);

        verify(bookingRepository).findAll(
            Mockito.<Specification<Booking>>any(),
            captor.capture()
        );

        Pageable pageable = captor.getValue();

        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(1);
        assertThat(pageable.getSort().getOrderFor("id").getDirection())
            .isEqualTo(Sort.Direction.ASC);
    }

    private void assertSorting(
        String orderBy,
        String expectedProperty,
        String order
    ) {
        // Arrange
        GetBookingListQuery query = createQuery(
            0,
            10,
            orderBy,
            order
        );

        when(bookingRepository.findAll(
            Mockito.<Specification<Booking>>any(),
            any(Pageable.class)
        )).thenReturn(Page.empty());

        // Act
        bookingCacheService.getBookings(query);

        // Assert
        ArgumentCaptor<Pageable> captor =
            ArgumentCaptor.forClass(Pageable.class);

        verify(bookingRepository).findAll(
            Mockito.<Specification<Booking>>any(),
            captor.capture()
        );

        Sort.Order sortOrder =
            captor.getValue().getSort().getOrderFor(expectedProperty);

        assertThat(sortOrder).isNotNull();

        assertThat(sortOrder.getDirection()).isEqualTo(
            "desc".equalsIgnoreCase(order)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC
        );
    }

    private GetBookingListQuery createQuery(
        int page,
        int pageSize,
        String orderBy,
        String order
    ) {
        GetBookingListQuery query = mock(GetBookingListQuery.class);

        when(query.page()).thenReturn(page);
        when(query.pageSize()).thenReturn(pageSize);
        when(query.orderBy()).thenReturn(orderBy);
        when(query.order()).thenReturn(order);

        return query;
    }
}
