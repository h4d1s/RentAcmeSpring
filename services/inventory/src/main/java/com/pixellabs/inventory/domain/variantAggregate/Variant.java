package com.pixellabs.inventory.domain.variantAggregate;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Variant extends AbstractAggregateRoot<Variant> {
	@Id
	private UUID id;
	private String name;

	@Enumerated(EnumType.STRING)
	private Gearbox gearbox;

	@Enumerated(EnumType.STRING)
	private FuelType fuelType;

	private int power;
	private int engineSize;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_id")
	private Model model;

	public Variant(
		String name,
		Gearbox gearbox,
		FuelType fuelType,
		int power,
		int engineSize,
		Model model) throws InventoryDomainException {
		super();

		this.id = UUID.randomUUID();
		this.updateName(name);
		this.updateGearbox(gearbox);
		this.updateFuelType(fuelType);
		this.updatePower(power);
		this.updateEngineSize(engineSize);
		this.model = model;
	}

	public void updateName(String name) throws InventoryDomainException {
		validateName(name);
		this.name = name;
	}

	public void updateGearbox(Gearbox gearbox) {
		this.gearbox = gearbox;
	}

	public void updateFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public void updatePower(int power) throws InventoryDomainException {
		validatePower(power);
		this.power = power;
	}

	public void updateEngineSize(int engineSize) throws InventoryDomainException {
		validateEngineSize(engineSize);
		this.engineSize = engineSize;
	}

	public void updateModel(Model model) throws InventoryDomainException {
		if (model == null) {
			throw new InventoryDomainException("Model cannot be null.");
		}

		this.model = model;
	}

	private void validateName(String name) throws InventoryDomainException {
		if (name == null || name.isBlank()) {
			throw new InventoryDomainException("Model name cannot be empty.");
		}
	}

	private void validatePower(int power) throws InventoryDomainException {
		if (power <= 0) {
			throw new InventoryDomainException("Power must be a positive integer.");
		}
	}

	private void validateEngineSize(int engineSize) throws InventoryDomainException {
		if (engineSize <= 0) {
			throw new InventoryDomainException("Engine size must be a positive integer.");
		}
	}
}
