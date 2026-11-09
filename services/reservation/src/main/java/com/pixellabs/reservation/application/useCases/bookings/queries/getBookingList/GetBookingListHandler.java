package com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList;

import com.pixellabs.reservation.application.services.BookingCacheService;
import com.pixellabs.reservation.application.useCases.bookings.dtos.BookingPageCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.application.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBookingListHandler {
	private final BookingCacheService bookingCacheService;

	@Transactional
	public Page<Booking> handle(GetBookingListQuery query)
			throws NotFoundException {
		BookingPageCache vehiclePageCache = bookingCacheService.getBookings(query);

		return new PageImpl<>(
			vehiclePageCache.content(),
			PageRequest.of(vehiclePageCache.page(), vehiclePageCache.size()),
			vehiclePageCache.totalElements()
		);
	}
}
