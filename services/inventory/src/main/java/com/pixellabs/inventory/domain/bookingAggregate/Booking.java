package com.pixellabs.inventory.domain.bookingAggregate;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends AbstractAggregateRoot<Booking> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private Status status;
	private LocalDate pickupDate;
	private LocalDate returnDate;
	
	@ManyToOne
    @JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;
	
	public Booking(Status status, LocalDate pickupDate, LocalDate returnDate, Vehicle vehicle) {
		super();
		this.id = UUID.randomUUID();
		this.status = status;
		this.pickupDate = pickupDate;
		this.returnDate = returnDate;
		this.vehicle = vehicle;
	}
	
	public void setPickupDate(LocalDate pickupDate) throws InventoryDomainException {
        if (status != Status.RESERVED) {
            throw new InventoryDomainException("Cannot set pickup date for a reservation that is not reserved.");
        }
        this.pickupDate = pickupDate;
    }

    public void setReturnDate(LocalDate returnDate) throws InventoryDomainException {
        if (status != Status.RESERVED) {
            throw new InventoryDomainException("Cannot set return date for a reservation that is not reserved.");
        }
        this.returnDate = returnDate;
    }

    public void setReservedStatus() {
        if (status == Status.RESERVED) {
            return;
        }
        status = Status.RESERVED;
    }

    public void setAvailableStatus() {
        if (status == Status.AVAILABLE) {
            return;
        }
        status = Status.AVAILABLE;
    }
}
