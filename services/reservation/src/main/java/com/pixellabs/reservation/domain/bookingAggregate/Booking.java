package com.pixellabs.reservation.domain.bookingAggregate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCanceledDomainEvent;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingCompletedDomainEvent;
import com.pixellabs.reservation.domain.bookingAggregate.events.BookingReservedDomainEvent;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Booking extends AbstractAggregateRoot<Booking> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private UUID vehicleId;
	private UUID userId;
	private LocalDate pickupDate;
	private LocalDate returnDate;
	private BookingStatus status;
	private LocalDateTime bookingDate;
	private BigDecimal price;
	
	public Booking(UUID vehicleId, UUID userId, LocalDate pickupDate, LocalDate returnDate) {
		super();
		this.id = UUID.randomUUID();
		this.vehicleId = vehicleId;
		this.userId = userId;
		this.pickupDate = pickupDate;
		this.returnDate = returnDate;
		this.status = BookingStatus.RESERVED;
		this.bookingDate = LocalDateTime.now();
		
		registerEvent(new BookingReservedDomainEvent(this, this));
	}
	
	public void setPrice(BigDecimal rentalPricePerDay) {
        long days = ChronoUnit.DAYS.between(pickupDate, returnDate) + 1;
        this.price = rentalPricePerDay.multiply(BigDecimal.valueOf(days));
    }

    public void setCompleteStatus() throws ReservationDomainException {
        if (status != BookingStatus.RESERVED) {
            statusChangeException(BookingStatus.COMPLETED);
        }

        this.status = BookingStatus.COMPLETED;
        registerEvent(new BookingCompletedDomainEvent(this));
    }

    public void setCanceledStatus() throws ReservationDomainException {
        if (status != BookingStatus.RESERVED) {
            statusChangeException(BookingStatus.CANCELED);
        }

        this.status = BookingStatus.CANCELED;
        registerEvent(new BookingCanceledDomainEvent(this));
    }
    
    private void statusChangeException(BookingStatus bookingStatusToChange) throws ReservationDomainException {
        throw new ReservationDomainException(
        	"It is not possible to change the booking status from %s to %s.".formatted(status, bookingStatusToChange)
        );
    }
}
