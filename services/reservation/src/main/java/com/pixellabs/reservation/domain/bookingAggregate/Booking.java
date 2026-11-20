package com.pixellabs.reservation.domain.bookingAggregate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking extends AbstractAggregateRoot<Booking> {
	@Id
	private UUID id;
	private UUID vehicleId;
	private UUID userId;
	private Instant pickupAt;
	private Instant returnAt;
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	private Instant bookingAt;
	private BigDecimal price;
	
	public Booking(
		UUID vehicleId,
		UUID userId,
		Instant pickupAt,
		Instant returnAt) throws ReservationDomainException {
		super();

		this.id = UUID.randomUUID();
		this.vehicleId = vehicleId;
		this.userId = userId;

		updatePickupAt(pickupAt);
		updateReturnAt(returnAt);

		this.status = BookingStatus.RESERVED;
		this.bookingAt = Instant.now();
		
		registerEvent(new BookingReservedDomainEvent(this));
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

	private void validatePickupAt(
		Instant pickupDate,
		Instant returnDate)
		throws ReservationDomainException {
		validateDateNotInPast(pickupDate);

		if (returnDate != null && !pickupDate.isBefore(returnDate)) {
			throw new ReservationDomainException("Pickup date must be earlier than the return date.");
		}
	}

	private void validateReturnAt(
		Instant returnDate,
		Instant pickupDate) throws ReservationDomainException {
		validateDateNotInPast(returnDate);
		if (pickupDate != null && !returnDate.isAfter(pickupDate)) {
			throw new ReservationDomainException("Return date must be later than the pickup date.");
		}
	}

	private void validateBookingAt(Instant bookingDate)
		throws ReservationDomainException {
		validateDateNotInPast(bookingDate);
	}

	private void validateDateNotInPast(Instant date)
		throws ReservationDomainException {
		if (date == null) {
			throw new ReservationDomainException("Date cannot be null.");
		}

		if (date.isBefore(Instant.now().minus(1, ChronoUnit.MINUTES))) {
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

	public void updatePickupAt(Instant pickupDate) throws ReservationDomainException {
		validatePickupAt(pickupDate, this.returnAt);
		this.pickupAt = pickupDate;
	}

	public void updateReturnAt(Instant returnDate) throws ReservationDomainException {
		validateReturnAt(returnDate, this.pickupAt);
		this.returnAt = returnDate;
	}

	public void updateBookingAt(Instant bookingDate) throws ReservationDomainException {
		if (bookingDate != null) {
			validateBookingAt(bookingDate);
		}

		this.bookingAt = bookingDate;
	}

	public void updatePrice(BigDecimal rentalPricePerDay)
		throws ReservationDomainException {
		validateRentalPrice(rentalPricePerDay);

		long days = ChronoUnit.DAYS.between(
			pickupAt,
			returnAt
		) + 1;

		this.price = rentalPricePerDay.multiply(
			BigDecimal.valueOf(days)
		);
	}
}
