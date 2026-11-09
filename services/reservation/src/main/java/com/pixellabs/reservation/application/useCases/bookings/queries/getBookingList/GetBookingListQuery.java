package com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList;

import com.pixellabs.reservation.application.validations.validVehicleId.ValidVehicleId;
import com.pixellabs.reservation.domain.bookingAggregate.BookingStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GetBookingListQuery(
	@Min(value = 1, message = "Page must be greater than or equal to 1")
	Integer page,

	@Min(value = 1, message = "Page size must be greater than or equal to 1")
	@Max(value = 100, message = "Page size must not exceed 100")
	Integer pageSize,

	@Pattern(
		regexp = "(?i)asc|desc",
		message = "Order must be either 'asc' or 'desc'"
	)
	String order,

	@Pattern(
		regexp = "(?i)id|pickupdate|returndate|status",
		message = "OrderBy must be one of: id, pickupDate, returnDate, status"
	)
	String orderBy,

	List<@NotNull UUID> userIds,

	@ValidVehicleId
	UUID vehicleId,

	@FutureOrPresent(message = "Pickup date must be in the future or present")
  	LocalDateTime pickupDate,

	@Future(message = "Return date must be in the future")
	LocalDateTime returnDate,

	BookingStatus status
) {
	public GetBookingListQuery {
        page = page != null ? Math.max(page, 1) : 1;
		pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
	}
}
