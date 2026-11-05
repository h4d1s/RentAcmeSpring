package com.pixellabs.inventory.domain.modelAggregate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.time.Year;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.FuelType;
import com.pixellabs.inventory.domain.variantAggregate.Gearbox;
import com.pixellabs.inventory.domain.variantAggregate.Variant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Model extends AbstractAggregateRoot<Model> {
	@Id
	private UUID id;
	private String name;
	private int yearOfProduction;
	private int numberOfSeats;

	@Enumerated(EnumType.STRING)
	private Category category;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
	private Brand brand;

	@JsonIgnore
	@OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
	private final Set<Variant> variants = new HashSet<>();

	public Model(
		String name,
		int yearOfProduction,
		int numberOfSeats,
		Category category,
		Brand brand) throws InventoryDomainException {
		super();

		this.id = UUID.randomUUID();
		this.updateName(name);
		this.updateYearOfProduction(yearOfProduction);
		this.updateNumberOfSeats(numberOfSeats);
		this.updateCategory(category);
		this.brand = brand;
	}

	public void addVariant(
			String name,
			Gearbox gearbox,
			FuelType fuelType,
			int power,
			int engineSize) throws InventoryDomainException {
        Variant variant = new Variant(name, gearbox, fuelType, power, engineSize, this);
        variants.add(variant);
    }

	public void addVariant(Variant variant) throws InventoryDomainException {
		if (variant == null) {
			throw new InventoryDomainException("Variant cannot be null.");
		}

		variant.updateModel(this);
		variants.add(variant);
	}
	
    public void removeVariant(UUID id) throws InventoryDomainException {
		boolean removed = variants.removeIf(v -> v.getId().equals(id));

		if (!removed) {
			throw new InventoryDomainException("Variant not found.");
		}
    }

	public void updateName(String newName) throws InventoryDomainException {
		validateName(newName);
		this.name = newName;
	}

	public void updateYearOfProduction(int yearOfProduction) throws InventoryDomainException {
		validateProductionYear(yearOfProduction);
		this.yearOfProduction = yearOfProduction;
	}

	public void updateNumberOfSeats(int numberOfSeats) throws InventoryDomainException {
		validateSeats(numberOfSeats);
		this.numberOfSeats = numberOfSeats;
	}

	public void updateCategory(Category category) {
		this.category = category;
	}

	public void updateBrand(Brand brand) throws InventoryDomainException {
		if (brand == null) {
			throw new InventoryDomainException("Brand cannot be null.");
		}

		this.brand = brand;
	}

	private void validateName(String name) throws InventoryDomainException {
		if (name == null || name.isBlank()) {
			throw new InventoryDomainException("Model name cannot be empty.");
		}
	}

	private void validateProductionYear(int year) throws InventoryDomainException {
		if (year < 1900 || year > Year.now(ZoneOffset.UTC).getValue() + 1) {
			throw new InventoryDomainException("Invalid production year: " + year);
		}
	}

	private void validateSeats(int seats) throws InventoryDomainException {
		if (seats <= 0) {
			throw new InventoryDomainException("Number of seats must be at least 1.");
		}
	}
}
