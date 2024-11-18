package com.pixellabs.inventory.domain.vehicleAggregate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pixellabs.inventory.domain.bookingAggregate.Booking;
import com.pixellabs.inventory.domain.bookingAggregate.Status;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.vehicleAggregate.events.VehicleCreatedDomainEvent;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends AbstractAggregateRoot<Vehicle> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private BigDecimal rentalPricePerDay;
	private String registrationPlates;
	
	@JsonIgnore
	@OneToMany(mappedBy = "vehicle")
	private Set<Booking> bookings;
	
	@JsonIgnore
	@OneToMany(mappedBy = "vehicle")
    private Set<Variant> variants;
	
	public Vehicle(BigDecimal rentalPricePerDay, String registrationPlates) throws InventoryDomainException {
		super();
		this.id = UUID.randomUUID();
		
		if(rentalPricePerDay.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InventoryDomainException("RentalPricePerDay must be greater than zero.");
		}
		
		this.rentalPricePerDay = rentalPricePerDay;
		
		if(registrationPlates.isEmpty()) {
			throw new InventoryDomainException("RegistrationPlates must not be empty.");
		}
		
		this.registrationPlates = registrationPlates;
		
		registerEvent(new VehicleCreatedDomainEvent(this.id));
	}
	
	public void addVariant(Variant variant) {
		variants.add(variant);
	}
	
	public void removeVariant(UUID id) throws InventoryDomainException {
		Optional<Variant> varantToRemove = variants.stream()
				.filter(each -> each.equals(id))
				.findAny();
		
        if (!varantToRemove.isPresent()) {
            throw new InventoryDomainException("Variant not found.");
        }

        variants.remove(varantToRemove.get());
	}
	
	public void addBooking(Status status, LocalDate pickupDate, LocalDate returnDate) {
		Booking booking = new Booking(status, pickupDate, returnDate, this);
		bookings.add(booking);
	}
	
	public void removeBooking(UUID id) throws InventoryDomainException {
		Optional<Booking> bookingToRemove = bookings.stream()
				.filter(each -> each.equals(id))
				.findAny();
		
        if (!bookingToRemove.isPresent()) {
            throw new InventoryDomainException("Booking not found.");
        }

        bookings.remove(bookingToRemove.get());
	}
}
