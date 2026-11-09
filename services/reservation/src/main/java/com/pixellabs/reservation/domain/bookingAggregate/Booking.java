package com.pixellabs.reservation.domain.bookingAggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCanceledDomainEvent;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCompletedDomainEvent;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingReservedDomainEvent;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Booking extends AbstractAggregateRoot<Booking> {
	@Id
	private UUID id;
	private UUID vehicleId;
	private UUID userId;
	private LocalDateTime pickupDate;
	private LocalDateTime returnDate;
	private BookingStatus status;
	private LocalDateTime bookingDate;
	private BigDecimal price;
	
	public Booking(
		UUID vehicleId,
		UUID userId,
		LocalDateTime pickupDate,
		LocalDateTime returnDate) throws ReservationDomainException {
		super();

		this.id = UUID.randomUUID();
		this.vehicleId = vehicleId;
		this.userId = userId;

		updatePickupDate(pickupDate);
		updateReturnDate(returnDate);

		this.status = BookingStatus.RESERVED;
		this.bookingDate = LocalDateTime.now();
		
		registerEvent(new BookingReservedDomainEvent(this, this));
	}

	public void updateCompleteStatus() throws ReservationDomainException {
		if (status != BookingStatus.RESERVED) {
			statusChangeException(BookingStatus.COMPLETED);
		}

		this.status = BookingStatus.COMPLETED;
		registerEvent(new BookingCompletedDomainEvent(this));
	}

	public void updateCanceledStatus() throws ReservationDomainException {
		if (status != BookingStatus.RESERVED) {
			statusChangeException(BookingStatus.CANCELED);
		}

		this.status = BookingStatus.CANCELED;
		registerEvent(new BookingCanceledDomainEvent(this));
	}

	private void validatePickupDate(
		LocalDateTime pickupDate,
		LocalDateTime returnDate)
		throws ReservationDomainException {
		validateDateNotInPast(pickupDate);

		if (returnDate != null && !pickupDate.isBefore(returnDate)) {
			throw new ReservationDomainException("Pickup date must be earlier than the return date.");
		}
	}

	private void validateReturnDate(
		LocalDateTime returnDate,
		LocalDateTime pickupDate) throws ReservationDomainException {
		validateDateNotInPast(returnDate);
		if (pickupDate != null && !returnDate.isAfter(pickupDate)) {
			throw new ReservationDomainException("Return date must be later than the pickup date.");
		}
	}

	private void validateBookingDate(LocalDateTime bookingDate)
		throws ReservationDomainException {
		validateDateNotInPast(bookingDate);
	}

	private void validateDateNotInPast(LocalDateTime date)
		throws ReservationDomainException {
		if (date == null) {
			throw new ReservationDomainException("Date cannot be null.");
		}

		if (date.isBefore(LocalDateTime.now().minusMinutes(1))) {
			throw new ReservationDomainException("Date cannot be in the past.");
		}
	}

	private void validateRentalPrice(BigDecimal rentalPricePerDay) throws ReservationDomainException {
		if (rentalPricePerDay == null ||
			rentalPricePerDay.compareTo(BigDecimal.ZERO) <= 0) {

			throw new ReservationDomainException("Rental price must be greater than zero.");
		}
	}

	private void statusChangeException(BookingStatus statusToChange)
		throws ReservationDomainException {
		throw new ReservationDomainException(
			"Is not possible to change the booking status from "
				+ status + " to " + statusToChange + "."
		);
	}

	public void updatePickupDate(LocalDateTime pickupDate) throws ReservationDomainException {
		validatePickupDate(pickupDate, this.returnDate);
		this.pickupDate = pickupDate;
	}

	public void updateReturnDate(LocalDateTime returnDate) throws ReservationDomainException {
		validateReturnDate(returnDate, this.pickupDate);
		this.returnDate = returnDate;
	}

	public void updateBookingDate(LocalDateTime bookingDate) throws ReservationDomainException {
		if (bookingDate != null) {
			validateBookingDate(bookingDate);
		}

		this.bookingDate = bookingDate;
	}

	public void updatePrice(BigDecimal rentalPricePerDay)
		throws ReservationDomainException {
		validateRentalPrice(rentalPricePerDay);

		long days = ChronoUnit.DAYS.between(
			pickupDate,
			returnDate
		) + 1;

		this.price = rentalPricePerDay.multiply(
			BigDecimal.valueOf(days)
		);
	}
}
