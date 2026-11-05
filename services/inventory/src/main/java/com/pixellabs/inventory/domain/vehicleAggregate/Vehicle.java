package com.pixellabs.inventory.domain.vehicleAggregate;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.vehicleAggregate.events.VehicleCreatedDomainEvent;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle extends AbstractAggregateRoot<Vehicle> {
    @Id
	private UUID id;
	private BigDecimal rentalPricePerDay;
	private String registrationPlates;

	@Enumerated(EnumType.STRING)
	private VehicleStatus status;
	private int mileageKm;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variant_id")
    private Variant variant;
	
	public Vehicle(
		BigDecimal rentalPricePerDay,
		String registrationPlates,
		VehicleStatus status,
		int mileageKm
	) throws InventoryDomainException {
		super();

		this.id = UUID.randomUUID();
		updateRentalPrice(rentalPricePerDay);
		updateRegistrationPlates(registrationPlates);
		updateMileageKm(mileageKm);
		updateStatus(status);
		
		registerEvent(new VehicleCreatedDomainEvent(this.id));
	}

    public void updateRentalPrice(BigDecimal price) throws InventoryDomainException {
		validateRentalPrice(price);
		this.rentalPricePerDay = price;
	}

	public void updateMileageKm(int mileageKm) throws InventoryDomainException {
		validateMileageKm(mileageKm);
		this.mileageKm = mileageKm;
	}

	public void updateRegistrationPlates(String plates) throws InventoryDomainException {
		validateRegistrationPlates(plates);
		this.registrationPlates = plates;
	}

	public void updateVariant(Variant variant) throws InventoryDomainException {
		if (variant == null) {
			throw new InventoryDomainException("Variant cannot be null.");
		}

		this.variant = variant;
	}

	public void updateStatus(VehicleStatus status) {
		this.status = status;
	}

	private void validateRentalPrice(BigDecimal price) throws InventoryDomainException  {
		if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InventoryDomainException("Rental price must be greater than zero.");
		}
	}

	private void validateRegistrationPlates(String plates) throws InventoryDomainException  {
		if (plates == null || plates.isBlank()) {
			throw new InventoryDomainException("Registration plates cannot be empty.");
		}
	}

	private void validateMileageKm(int mileageKm) throws InventoryDomainException  {
		if (mileageKm < 0) {
			throw new InventoryDomainException("Mileage must be greater than zero.");
		}
	}
}
