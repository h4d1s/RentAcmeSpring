package com.pixellabs.inventory.domain.brandAggregate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Category;
import com.pixellabs.inventory.domain.modelAggregate.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {
	@Id
	private UUID id;
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
	private final Set<Model> models = new HashSet<>();
	
	public Brand(String name) throws InventoryDomainException {
		super();

		this.id = UUID.randomUUID();
		this.updateName(name);
	}
	
	public void addModel(Model model) throws InventoryDomainException {
		if (model == null) {
			throw new InventoryDomainException("Model cannot be null.");
		}

		model.updateBrand(this);
		models.add(model);
	}

	public void addModel(
		String name,
		int yearOfProduction,
		int numberOfSeats,
		Category category
	) throws InventoryDomainException {
		addModel(new Model(name, yearOfProduction, numberOfSeats, category, this));
	}
	
	public void removeModel(UUID id) throws InventoryDomainException {
		boolean removed = models.removeIf(model -> model.getId().equals(id));

		if (!removed) {
			throw new InventoryDomainException("Model not found.");
		}
	}

	public void updateName(String newName) throws InventoryDomainException {
		validateName(newName);
		this.name = newName;
	}

	private void validateName(String name) throws InventoryDomainException {
		if (name == null || name.isBlank()) {
			throw new InventoryDomainException("The brand name cannot be empty.");
		}
	}
}
