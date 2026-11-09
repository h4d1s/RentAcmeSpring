package com.pixellabs.reservation.application.services;

import com.pixellabs.reservation.application.useCases.bookings.dtos.BookingPageCache;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList.GetBookingListQuery;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.bookingAggregate.specifications.BookingSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingCacheService {
  private final BookingRepository bookingRepository;

  @Cacheable(
    value = "bookings",
    key = "'list:'"
      + " + #query.page()"
      + " + ':' + #query.pageSize()"
      + " + ':' + #query.orderBy()"
      + " + ':' + #query.order()"
      + " + ':' + #query.userIds()"
      + " + ':' + #query.vehicleId()"
      + " + ':' + #query.pickupDate()"
      + " + ':' + #query.returnDate()"
      + " + ':' + #query.status()"
  )
  @Transactional
  public BookingPageCache getBookings(GetBookingListQuery query) {
    int page = Math.max(query.page(), 1);
    int pageSize = Math.max(query.pageSize(), 1);

    String orderBy = query.orderBy() == null
        ? "id"
        : query.orderBy().trim().toLowerCase();
    String sortBy = switch (orderBy) {
      case "pickupdate" -> "pickupDate";
      case "returndate" -> "returnDate";
      case "status" -> "status";
      default -> "id";
    };

    Sort.Direction direction = "desc".equalsIgnoreCase(query.order())
      ? Sort.Direction.DESC
      : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(
      page - 1,
      pageSize,
      Sort.by(direction, sortBy)
    );
    Page<Booking> result = bookingRepository.findAll(
        BookingSpecifications.withFilters(
            query.vehicleId(),
            query.userIds(),
            query.pickupDate(),
            query.returnDate(),
            query.status()
        ),
        pageable
    );

    return new BookingPageCache(
      result.getContent(),
      result.getNumber(),
      result.getSize(),
      result.getTotalElements(),
      result.getTotalPages()
    );
  }
}
