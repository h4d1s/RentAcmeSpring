package com.pixellabs.inventory.domain.brandAggregate;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Category;
import com.pixellabs.inventory.domain.modelAggregate.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	
	@JsonIgnore
	@OneToMany
	private Set<Model> models;
	
	public Brand(String name) throws InventoryDomainException {
		super();
		
		if(name.isEmpty()) {
			throw new InventoryDomainException("Brand name cannot be empty.");
		}
		
		this.name = name;
	}
	
	public void addModel(String name, int yearOfProduction, int numberOfSeats, Category category) throws InventoryDomainException {
		Model model = new Model(name, yearOfProduction, numberOfSeats, category, this);
		models.add(model);
	}
	
	public void addModel(Model model) {
		models.add(model);
	}
	
	public void removeModel(UUID id) throws InventoryDomainException {
		Optional<Model> modelToRemove = models.stream()
				.filter(each -> each.equals(id))
				.findAny();
		
        if (!modelToRemove.isPresent()) {
            throw new InventoryDomainException("Model not found.");
        }

        models.remove(modelToRemove.get());
	}
}
